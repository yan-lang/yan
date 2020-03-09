package yan.foundation.driver;

import yan.foundation.compiler.frontend.ir.IRProgram;
import yan.foundation.compiler.frontend.lex.Token;

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

    protected Phase<String, List<Token>> lexer;
    protected Phase<List<Token>, Tree> parser;
    protected List<Phase<Tree, Tree>> semAnalyzers = new ArrayList<>();
    protected Phase<Tree, IRProgram> irTranslator;
    protected List<Phase<IRProgram, IRProgram>> optimizers = new ArrayList<>();
//    protected Phase<IRProgram, String> llvmIRTranslator;

    private Map<String, Task<String, ?>> target2Phase = new HashMap<>();

    protected void buildCompilerTargets() {
        if (lexer == null) return;
        if (lexer.printer != null) {
            target2Phase.put(lexer.printer.targetName(), lexer);
            compilerTargets.add(lexer.printer.targetName());
        }
        if (parser == null) return;
        if (parser.printer != null) {
            target2Phase.put(parser.printer.targetName(), lexer.then(parser));
            compilerTargets.add(parser.printer.targetName());
        }
        if (semAnalyzers.isEmpty()) return;
        var semTask = lexer.then(parser);
        for (var semAnalyzer : semAnalyzers) {
            semTask = semTask.then(semAnalyzer);
            if (semAnalyzer.printer != null) {
                target2Phase.put(semAnalyzer.printer.targetName(), semTask);
                compilerTargets.add(semAnalyzer.printer.targetName());
            }
        }
        if (irTranslator == null) return;
        if (irTranslator.printer != null) {
            target2Phase.put(irTranslator.printer.targetName(), semTask.then(irTranslator));
            compilerTargets.add(irTranslator.printer.targetName());
        }
        var optTask = semTask.then(irTranslator);
        for (var optimizer : optimizers) {
            optTask = optTask.then(optimizer);
            if (optimizer.printer != null) {
                target2Phase.put(optimizer.printer.targetName(), optTask);
                compilerTargets.add(optimizer.printer.targetName());
            }
        }
    }

    public int compile() {
        Phase<String, ?> task = (Phase<String, ?>) target2Phase.get(config.target);
        task.apply(config.source);
        return task.errorCollector.numOfErrors();
    }
}
