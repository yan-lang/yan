package yan.foundation;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.middlend.instruction.IRProgram;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.driver.Task;
import yan.foundation.driver.error.ErrorCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Language<TopLevel> implements BaseConfig.compilerTargetProvider {

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

    protected Map<String, Task<String, ?>> target2Task = new HashMap<>();

    protected Phase<String, List<Token>> lexer;
    protected Phase<List<Token>, TopLevel> parser;
    protected List<Phase<TopLevel, TopLevel>> semAnalyzers = new ArrayList<>();
    protected Phase<TopLevel, IRProgram> irTranslator;
    protected List<Phase<IRProgram, IRProgram>> optimizers = new ArrayList<>();
//    protected Phase<IRProgram, String> llvmIRTranslator;

    protected List<Phase<?, ?>> phases = new ArrayList<>();

    protected void buildCompilerTargets() {
        checkAndPutTarget(lexer, lexer);
        checkAndPutTarget(parser, lexer.then(parser));

        if (semAnalyzers.isEmpty()) return;
        var semTask = lexer.then(parser);
        for (var semAnalyzer : semAnalyzers) {
            semTask = semTask.then(semAnalyzer);
            checkAndPutTarget(semAnalyzer, semTask);
        }

        checkAndPutTarget(irTranslator, semTask.then(irTranslator));

        var optTask = semTask.then(irTranslator);
        for (var optimizer : optimizers) {
            optTask = optTask.then(optimizer);
            checkAndPutTarget(optimizer, optTask);
        }
    }

    private void checkAndPutTarget(Phase<?, ?> phase, Task<String, ?> task) {
        if (phase == null) return;
        phase.getFormatter().ifPresent(formatter -> {
            target2Task.put(formatter.targetName(), task);
            compilerTargets.add(formatter.targetName());
        });
    }

    // ---------------------- Core Functionality ---------------------- //

    public int compile() {
        Task<String, ?> task = target2Task.get(config.target);
        task.apply(config.source);
        config.out.close();
        return ErrorCollector.shared.numOfErrors();
    }

}
