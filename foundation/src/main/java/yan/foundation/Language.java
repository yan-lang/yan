package yan.foundation;

import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.driver.Task;
import yan.foundation.driver.error.ErrorCollector;
import yan.foundation.interpreter.Interpretable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Language implements BaseConfig.compilerTargetProvider, Interpretable {

    // ------------------- Basic Configuration ------------------- //

    public BaseConfig config;

    public Language() {
        this.config = new BaseConfig();
        setupConfig();
    }

    public Language(BaseConfig config) {
        this.config = config;
        setupConfig();
    }

    protected void setupConfig() {
        BaseConfig.CompilerTargetCandidates.provider = this;
        BaseConfig.DefaultProvider.provider = this;
    }

    // ------------------- Compiler Targets ------------------- //

    protected List<String> compilerTargets = new ArrayList<>();

    public List<String> getCompilerTargets() {
        return compilerTargets;
    }

    public String getDefaultCompilerTarget() {
        return compilerTargets.isEmpty() ? null : compilerTargets.get(compilerTargets.size() - 1);
    }

    // ------------------- Compiler Tasks ------------------- //

    protected Map<String, Task<?, ?>> tasks = new HashMap<>();

    protected List<Phase<?, ?>> phases = new ArrayList<>();

    public Language addPhase(Phase<?, ?> phase, String targetName) throws Exception {
        phases.add(phase);
        compilerTargets.add(targetName);
        if (tasks.isEmpty()) tasks.put(targetName, phase);
        else tasks.put(targetName, phases.get(phases.size() - 2).then(phase.get()));
        return this;
    }

    // ---------------------- Core Functionality ---------------------- //

    public int compile() {
        Task<String, ?> task = (Task<String, ?>) tasks.get(config.target);
        task.apply(config.source);
        config.out.close();
        return ErrorCollector.shared.numOfErrors();
    }

    @Override
    public Object execute(String statement, PrintWriter out) throws Exception {
        config.out = out;
        config.err = out;
        config.target = getDefaultCompilerTarget();
        Task<String, ?> task = (Task<String, ?>) tasks.get(config.target);
        // clear errors
        ErrorCollector.shared.clean();
        try {
            Object output = task.apply(statement);
        } catch (RuntimeException e) {
            e.printStackTrace(out);
        }
        // TODO: check if output is a R-Value, if it is, return the value.
        return null;
    }
}
