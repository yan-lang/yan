package yan.foundation.driver;

import yan.foundation.compiler.frontend.ir.IRProgram;
import yan.foundation.compiler.frontend.lex.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Language<Tree> {

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

    private void setupConfig() {
        BaseConfig.CompilerTargetCandidates.language = this;
        BaseConfig.DefaultProvider.language = this;
    }

    // ------------------- Compiler Targets ------------------- //

    private List<String> compilerTargets = new ArrayList<>();

    public List<String> getCompilerTargets() {
        return compilerTargets;
    }

    public String getDefaultCompilerTarget() {
        return compilerTargets.isEmpty() ? null : compilerTargets.get(compilerTargets.size() - 1);
    }

    protected Phase<String, List<Token>> lexer;
    protected Phase<List<Token>, Tree> parser;
    protected List<Phase<Tree, Tree>> semAnalyzers = new ArrayList<>();
    protected Phase<Tree, IRProgram> irTranslator;
    protected List<Phase<IRProgram, IRProgram>> optimizers = new ArrayList<>();
//    protected Phase<IRProgram, String> llvmIRTranslator;

    private Map<String, Task<String, ?>> target2Phase = new HashMap<>();

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
        if (phase.printer != null) {
            target2Phase.put(phase.printer.targetName(), task);
            compilerTargets.add(phase.printer.targetName());
        }
    }

    // ---------------------- Core Functionality ---------------------- //

    public int compile() {
        Phase<String, ?> task = (Phase<String, ?>) target2Phase.get(config.target);
        task.apply(config.source);
        return task.errorCollector.numOfErrors();
    }
}
