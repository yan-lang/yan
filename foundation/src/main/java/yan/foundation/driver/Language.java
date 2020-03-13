package yan.foundation.driver;

import yan.foundation.compiler.frontend.ir.IRProgram;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.error.ErrorCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Language<TopLevel> {

    // ------------------- Basic Configuration ------------------- //

    public BaseConfig config;

    public Language(BaseConfig config) {
        this.config = config;
        setupConfig();
    }

    public Language() {
        this.config = new BaseConfig();
        setupConfig();
    }

    protected List<String> compilerTargets = new ArrayList<>();

    // ------------------- Compiler Targets ------------------- //

    protected Map<String, Task<String, ?>> target2Task = new HashMap<>();

    public List<String> getCompilerTargets() {
        return compilerTargets;
    }

    public String getDefaultCompilerTarget() {
        return compilerTargets.isEmpty() ? null : compilerTargets.get(compilerTargets.size() - 1);
    }

    protected Phase<String, List<Token>> lexer;
    protected Phase<List<Token>, TopLevel> parser;
    protected List<Phase<TopLevel, TopLevel>> semAnalyzers = new ArrayList<>();
    protected Phase<TopLevel, IRProgram> irTranslator;
    protected List<Phase<IRProgram, IRProgram>> optimizers = new ArrayList<>();
//    protected Phase<IRProgram, String> llvmIRTranslator;

    protected void setupConfig() {
        BaseConfig.CompilerTargetCandidates.language = this;
        BaseConfig.DefaultProvider.language = this;
    }

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
        if (phase.formatter != null) {
            target2Task.put(phase.formatter.targetName(), task);
            compilerTargets.add(phase.formatter.targetName());
        }
    }

    // ---------------------- Core Functionality ---------------------- //

    public int compile() {
        Task<String, ?> task = target2Task.get(config.target);
        task.apply(config.source);
        config.out.close();
        return ErrorCollector.shared.numOfErrors();
    }
}
