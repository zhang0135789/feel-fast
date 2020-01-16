package com.feel.common.utils.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Term implements Cloneable {
    private String column;
    private Object value;
    private Term.Type type;
    private String termType;
    private List<String> options;
    private List<Term> terms;

    public Term() {
        this.type = Term.Type.and;
        this.termType = "eq";
        this.options = new ArrayList();
        this.terms = new LinkedList();
    }

    public Term or(String term, Object value) {
        return this.or(term, "eq", value);
    }

    public Term and(String term, Object value) {
        return this.and(term, "eq", value);
    }

    public Term or(String term, String termType, Object value) {
        Term queryTerm = new Term();
        queryTerm.setTermType(termType);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        queryTerm.setType(Term.Type.or);
        this.terms.add(queryTerm);
        return this;
    }

    public Term and(String term, String termType, Object value) {
        Term queryTerm = new Term();
        queryTerm.setTermType(termType);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        queryTerm.setType(Term.Type.and);
        this.terms.add(queryTerm);
        return this;
    }

    public Term nest() {
        return this.nest((String)null, (Object)null);
    }

    public Term orNest() {
        return this.orNest((String)null, (Object)null);
    }

    public Term nest(String term, Object value) {
        Term queryTerm = new Term();
        queryTerm.setType(Term.Type.and);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        this.terms.add(queryTerm);
        return queryTerm;
    }

    public Term orNest(String term, Object value) {
        Term queryTerm = new Term();
        queryTerm.setType(Term.Type.or);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        this.terms.add(queryTerm);
        return queryTerm;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        if (column != null) {
            if (column.contains("$")) {
                String[] tmp = column.split("[$]");
                this.setTermType(tmp[1]);
                column = tmp[0];
                if (tmp.length > 2) {
                    this.options.addAll(Arrays.asList(tmp).subList(2, tmp.length));
                }
            }

            this.column = column;
        }
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Term.Type getType() {
        return this.type;
    }

    public void setType(Term.Type type) {
        this.type = type;
    }

    public String getTermType() {
        return this.termType.toLowerCase();
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public List<Term> getTerms() {
        return this.terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public Term addTerm(Term term) {
        this.terms.add(term);
        return this;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Term clone() {
        Term term = new Term();
        term.setColumn(this.column);
        term.setValue(this.value);
        term.setTermType(this.termType);
        term.setType(this.type);
        this.terms.forEach((t) -> {
            term.addTerm(t.clone());
        });
        term.setOptions(new ArrayList(this.getOptions()));
        return term;
    }

    public static enum Type {
        or,
        and;

        private Type() {
        }

        public static Term.Type fromString(String str) {
            try {
                return valueOf(str.toLowerCase());
            } catch (Exception var2) {
                return and;
            }
        }
    }
}
