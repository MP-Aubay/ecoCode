package fr.greencodeinitiative.html.rules;

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.html.api.HtmlConstants;
import org.sonar.plugins.html.rules.SonarWayProfile;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public class HtmlRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "ecocode-html";
    public static final String REPOSITORY_NAME = "ecoCode";
    
    private static final String METADATA_PATH = "fr/greencodeinitiative/l10n/html";
    
    private final SonarRuntime sonarRuntime;

    public HtmlRulesDefinition(SonarRuntime sonarRuntime) {
    	this.sonarRuntime = sonarRuntime;
    }
    
    @Override
    public void define(Context context) {
		NewRepository repository = context.createRepository(REPOSITORY_KEY, HtmlConstants.LANGUAGE_KEY).setName(REPOSITORY_NAME);
		
		RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(METADATA_PATH, SonarWayProfile.JSON_PROFILE_PATH, this.sonarRuntime);
		
		ruleMetadataLoader.addRulesByAnnotatedClass(repository, CheckClasses.getCheckClasses());
		
        repository.done();
    }

}
