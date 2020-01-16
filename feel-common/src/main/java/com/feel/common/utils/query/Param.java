package com.feel.common.utils.query;

import com.feel.modules.app.query.Term.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Param implements Cloneable {
    protected List<Term> terms = new LinkedList();

    public Param() {
    }

    public <T extends Param> T or(String column, Object value) {
        return this.or(column, "eq", value);
    }

    public <T extends Param> T and(String column, Object value) {
        return this.and(column, "eq", value);
    }

    public <T extends Param> T or(String column, String termType, Object value) {
        Term term = new Term();
        term.setTermType(termType);
        term.setColumn(column);
        term.setValue(value);
        term.setType(Type.or);
        this.terms.add(term);
        return (T)this;
    }

    public <T extends Param> T and(String column, String termType, Object value) {
        Term term = new Term();
        term.setTermType(termType);
        term.setColumn(column);
        term.setValue(value);
        term.setType(Type.and);
        this.terms.add(term);
        return (T)this;
    }

    public Term nest() {
        return this.nest((String)null, (Object)null);
    }

    public Term orNest() {
        return this.orNest((String)null, (Object)null);
    }

    public Term nest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Type.and);
        this.terms.add(term);
        return term;
    }

    public Term orNest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Type.or);
        this.terms.add(term);
        return term;
    }

    public <T extends Param> T where(String key, Object value) {
        this.and(key, value);
        return (T)this;
    }

    public <T extends Param> T where(String key, String termType, Object value) {
        this.and(key, termType, value);
        return (T)this;
    }

    public List<Term> getTerms() {
        return this.terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public <T extends Param> T addTerm(Term term) {
        this.terms.add(term);
        return (T)this;
    }

    public Param clone() {
        Param param = new Param();
        List<Term> terms = (List)this.terms.stream().map((term) -> {
            return term.clone();
        }).collect(Collectors.toList());
        param.setTerms(terms);
        return param;
    }
}

