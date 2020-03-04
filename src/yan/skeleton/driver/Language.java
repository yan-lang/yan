package yan.skeleton.driver;

import yan.skeleton.compiler.frontend.ir.IRProgram;
import yan.skeleton.compiler.frontend.lex.LexerToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Language<Tree> {
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

    private List<String> compilerTargets = new ArrayList<>();

    public List<String> getCompilerTargets() {
        return compilerTargets;
    }

    public String getDefaultCompilerTarget() {
        return compilerTargets.isEmpty() ? null : compilerTargets.get(compilerTargets.size() - 1);
    }

    protected Phase<String, List<LexerToken>> lexer;
    protected Phase<List<LexerToken>, Tree> parser;
    protected List<Phase<Tree, Tree>> semAnalyzers = new ArrayList<>();
    protected Phase<Tree, IRProgram> irTranslator;
    protected List<Phase<IRProgram, IRProgram>> optimizers = new ArrayList<>();
//    protected Phase<IRProgram, String> llvmIRTranslator;

    private Map<String, Task<String, ?>> target2Phase = new HashMap<>();

    protected void buildCompilerTargets() {
        if (lexer == null) return;
        if (lexer instanceof PrintablePhase) {
            target2Phase.put(((PrintablePhase) lexer).targetName(), lexer);
        }
        if (parser == null) return;
        if (parser instanceof PrintablePhase) {
            target2Phase.put(((PrintablePhase) parser).targetName(), lexer.then(parser));
        }
        if (semAnalyzers.isEmpty()) return;
        var semTask = lexer.then(parser);
        for (var semAnalyzer : semAnalyzers) {
            semTask = semTask.then(semAnalyzer);
            if (semAnalyzer instanceof PrintablePhase) {
                target2Phase.put(((PrintablePhase) semAnalyzer).targetName(), semTask);
            }
        }
        if (irTranslator == null) return;
        if (irTranslator instanceof PrintablePhase) {
            target2Phase.put(((PrintablePhase) irTranslator).targetName(), semTask.then(irTranslator));
        }
        var optTask = semTask.then(irTranslator);
        for (var optimizer : optimizers) {
            optTask = optTask.then(optimizer);
            if (optimizer instanceof PrintablePhase) {
                target2Phase.put(((PrintablePhase) optimizer).targetName(), optTask);
            }
        }
    }

    public int compile() {
        Phase<String, ?> task = (Phase<String, ?>) target2Phase.get(config.target);
        task.apply(config.source);
        return task.errorCollector.numOfErrors();
    }
}
