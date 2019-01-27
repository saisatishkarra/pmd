/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.apex;

import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.pmd.lang.AbstractPmdLanguageVersionHandler;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.VisitorStarter;
import net.sourceforge.pmd.lang.XPathHandler;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClassOrInterface;
import net.sourceforge.pmd.lang.apex.ast.ApexNode;
import net.sourceforge.pmd.lang.apex.ast.DumpFacade;
import net.sourceforge.pmd.lang.apex.metrics.ApexMetricsComputer;
import net.sourceforge.pmd.lang.apex.metrics.api.ApexClassMetricKey;
import net.sourceforge.pmd.lang.apex.metrics.api.ApexOperationMetricKey;
import net.sourceforge.pmd.lang.apex.multifile.ApexMultifileVisitorFacade;
import net.sourceforge.pmd.lang.apex.rule.ApexRuleViolationFactory;
import net.sourceforge.pmd.lang.ast.xpath.DefaultASTXPathHandler;
import net.sourceforge.pmd.lang.metrics.LanguageMetricsProvider;
import net.sourceforge.pmd.lang.metrics.internal.AbstractLanguageMetricsProvider;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

public class ApexHandler extends AbstractPmdLanguageVersionHandler {

    private final ApexMetricsProvider myMetricsProvider = new ApexMetricsProvider();


    @Override
    public VisitorStarter getMultifileFacade() {
        return rootNode -> new ApexMultifileVisitorFacade().initializeWith((ApexNode<?>) rootNode);
    }


    @Override
    public XPathHandler getXPathHandler() {
        return new DefaultASTXPathHandler();
    }

    @Override
    public RuleViolationFactory getRuleViolationFactory() {
        return ApexRuleViolationFactory.INSTANCE;
    }

    @Override
    public ParserOptions getDefaultParserOptions() {
        return new ApexParserOptions();
    }

    @Override
    public Parser getParser(ParserOptions parserOptions) {
        return new ApexParser(parserOptions);
    }

    @Override
    public VisitorStarter getDumpFacade(Writer writer, String prefix, boolean recurse) {
        return rootNode -> new DumpFacade().initializeWith(writer, prefix, recurse, (ApexNode<?>) rootNode);
    }


    @Override
    public LanguageMetricsProvider<ASTUserClassOrInterface<?>, ASTMethod> getLanguageMetricsProvider() {
        return myMetricsProvider;
    }


    private static class ApexMetricsProvider extends AbstractLanguageMetricsProvider<ASTUserClassOrInterface<?>, ASTMethod> {

        @SuppressWarnings("unchecked")
        ApexMetricsProvider() {
            // a wild double cast
            super((Class<ASTUserClassOrInterface<?>>) (Object) ASTUserClassOrInterface.class, ASTMethod.class, ApexMetricsComputer.getInstance());
        }


        @Override
        public List<ApexClassMetricKey> getAvailableTypeMetrics() {
            return Arrays.asList(ApexClassMetricKey.values());
        }


        @Override
        public List<ApexOperationMetricKey> getAvailableOperationMetrics() {
            return Arrays.asList(ApexOperationMetricKey.values());
        }
    }
}
