package yan.skeleton.driver;

import yan.skeleton.compiler.frontend.lex.AbstractLexer;
import yan.skeleton.compiler.frontend.parse.AbstractParser;
import yan.skeleton.compiler.frontend.semantic.AbstractSemAnalyzer;

import java.util.ArrayList;
import java.util.List;


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

    protected List<String> compilerTargets = new ArrayList<>();

    protected List<Phase<?, ?>> compilerPhases = new ArrayList<>();

    public List<String> getCompilerTargets() {
        return compilerTargets;
    }

    public String getDefaultCompilerTarget() {
        return compilerPhases.isEmpty() ? null : compilerTargets.get(compilerTargets.size() - 1);
    }

    protected void addPhase(Phase<?, ?> phase){
        addPhase(phase, phase.name.toLowerCase());
    }

    protected void addPhase(Phase<?, ?> phase, String targetName) {
        compilerPhases.add(phase);
        compilerTargets.add(targetName);
    }
}
