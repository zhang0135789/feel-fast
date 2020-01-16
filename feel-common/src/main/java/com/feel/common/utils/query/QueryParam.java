package com.feel.common.utils.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryParam<K> extends Param implements Serializable, Cloneable {
    private static final long serialVersionUID = 7941767360194797891L;
    private int pageIndex = 0;
    private int pageSize = 25;
    private List<Sort> sorts = new LinkedList();

    public QueryParam() {
    }

    public Sort orderBy(String column) {
        Sort sort = new Sort(column);
        this.sorts.add(sort);
        return sort;
    }

    public <Q extends QueryParam> Q doPaging(int pageIndex) {
        this.pageIndex = pageIndex;
        return (Q)this;
    }

    public <Q extends QueryParam> Q doPaging(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        return (Q)this;
    }

    public <Q extends QueryParam> Q rePaging(int total) {
        if (this.getPageIndex() != 0 && this.pageIndex * this.pageSize >= total) {
            int tmp = total / this.getPageSize();
            this.pageIndex = total % this.getPageSize() == 0 ? tmp - 1 : tmp;
        }

        return (Q)this;
    }

    public Page<K> getPage(){
        return new Page<>(
                getPageIndex(),
                getPageSize()
        );
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }


    @ApiParam(hidden = true)
    public QueryWrapper<K> getWrapper(){
        QueryWrapper<K> query = new QueryWrapper<K>();
        query.eq("1",1);
        if(terms.size() > 0 ){
            Iterator<Term> it = terms.iterator();
            while(it.hasNext()){
                Term term = it.next();
                if(StringUtils.isNotBlank(term.getColumn())){
                    switch (term.getType()){
                        case and:
                            query.and(queryWrapper->getQueryWrapperByOp(term,queryWrapper));
                            break;
                        case or:
                            query.or(queryWrapper->getQueryWrapperByOp(term,queryWrapper));
                            break;

                    }
                }
            }
        }

        if(sorts.size() > 0 ){
            Iterator<Sort> it = sorts.iterator();
            List<String> orderByAscs = new ArrayList<>();
            List<String> orderByDescs = new ArrayList<>();

            while (it.hasNext()){
                Sort sort = it.next();

                String column = HumpToUnderline(sort.getName());
                switch (sort.getOrder()){
                    case SortType.ASC:
                        orderByAscs.add(column);
                        break;
                    case SortType.DESC:
                        orderByDescs.add(column);
                        break;
                }

                query.orderByAsc(orderByAscs.toArray(new String[orderByAscs.size()]));
                query.orderByDesc(orderByDescs.toArray(new String[orderByDescs.size()]));
            }
        }

        return query;
    }

    private QueryWrapper<K> getQueryWrapperByOp(Term term, QueryWrapper<K> queryWrapper){
        String column = HumpToUnderline(term.getColumn());
        switch (term.getTermType()){
            case TermType.EQ:
                queryWrapper.eq(column,term.getValue());
                break;
            case TermType.LT:
                queryWrapper.lt(column,term.getValue());
                break;
            case TermType.LTE:
                queryWrapper.le(column,term.getValue());
                break;
            case TermType.GT:
                queryWrapper.gt(column,term.getValue());
                break;
            case TermType.GTE:
                queryWrapper.ge(column,term.getValue());
                break;
            case TermType.START_LIKE:
                queryWrapper.likeLeft(column,term.getValue());
                break;
            case TermType.END_LIKE:
                queryWrapper.likeRight(column,term.getValue());
                break;
            case TermType.LIKE:
                queryWrapper.like(column,term.getValue());
                break;
            case TermType.IN:
                queryWrapper.in(column,StringUtils.split(String.valueOf(term.getValue()),","));
                break;
        }

        return queryWrapper;
    }


    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Sort> getSorts() {
        return this.sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public QueryParam clone() {
        QueryParam sqlParam = new QueryParam();
        List<Term> terms = (List)this.terms.stream().map(Term::clone).collect(Collectors.toList());
        sqlParam.setTerms(terms);
        sqlParam.setPageIndex(this.pageIndex);
        sqlParam.setPageSize(this.pageSize);
        sqlParam.setSorts(this.sorts);
        return sqlParam;
    }
}
