package com.nd.gaea.odata.uri.parser;

import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.Parser;

import java.util.BitSet;

class CheckFullContextListener extends DiagnosticErrorListener {

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line,
                            final int charPositionInLine,
                            final String msg, final RecognitionException e) {
        // System.err.println("syntaxError detected");
    }

    @Override
    public void reportAmbiguity(final Parser recognizer, final DFA dfa, final int startIndex, final int stopIndex,
                                final boolean exact,
                                final BitSet ambigAlts, final ATNConfigSet configs) {
        // System.err.println("reportAmbiguity detected");
    }

    @Override
    public void reportAttemptingFullContext(final Parser recognizer, final DFA dfa, final int startIndex,
                                            final int stopIndex,
                                            final BitSet conflictingAlts, final ATNConfigSet configs) {
        // System.err.println("reportAttemptingFullContext detected");
    }

    @Override
    public void reportContextSensitivity(final Parser recognizer, final DFA dfa, final int startIndex,
                                         final int stopIndex, final int prediction,
                                         final ATNConfigSet configs) {
        // System.err.println("reportContextSensitivity detected");
    }

}