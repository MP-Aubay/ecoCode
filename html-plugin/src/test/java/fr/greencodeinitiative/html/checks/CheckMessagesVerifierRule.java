package fr.greencodeinitiative.html.checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.rules.Verifier;
import org.sonar.plugins.html.checks.HtmlIssue;

public class CheckMessagesVerifierRule extends Verifier {

    private final List<CheckMessagesVerifier> verifiers = new ArrayList<>();

    public CheckMessagesVerifier verify(Collection<HtmlIssue> messages) {
      CheckMessagesVerifier verifier = CheckMessagesVerifier.verify(messages);
      verifiers.add(verifier);
      return verifier;
    }

    @Override
    protected void verify() {
      for (CheckMessagesVerifier verifier : verifiers) {
        verifier.noMore();
      }
    }

  }