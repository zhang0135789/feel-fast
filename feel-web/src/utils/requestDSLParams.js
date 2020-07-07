/* eslint-disable */
// const moment = require('moment')

import moment from 'moment'


const DSLParameterBuilder = () => {
  const me = {}
  me.param = {}
  me.terms = []
  me.sorts = []
  me.nowType = 'and'
  function bindOperate(operate) {
    function accept(k, t, v) {
      if (v && typeof v !== 'string' || (v && typeof v === 'string' && v.indexOf('undefined') === -1)) {
        operate.terms.push({ column: k, type: operate.nowType, termType: t, value: v })
      }
      return operate
    }
    const mapping = [
      'gt', 'gte', 'lt', 'lte', 'like', 'nlike', 'in', 'is', 'eq'
    ]
    mapping.forEach((type) => {
      operate[type] = (k, v) => accept(k, type, v)
    })
    const mapping2 = [
      'isnull', 'notnull', 'empty', 'nempty'
    ]
    mapping2.forEach((type) => {
      operate[type] = k => accept(k, type, '1')
    })
    const mapping3 = ['not']
    mapping3.forEach((type) => {
      operate[type] = (k, v) => accept(k, 'not', v)
    })

    operate.btw = operate.between = (column, time) => {
      if (time) {
        const startTime = moment(time).format('YYYY-MM-DD 00:00:00')
        const endTime = moment(time).format('YYYY-MM-DD 23:59:59')
        return accept(column, 'btw', `${startTime},${endTime}`)
      } return accept(column, 'btw')
    }

    operate.nbtw = operate.notBetween = (column, between, and) => {
      if (between && and) {
        return accept(column, 'nbtw', `${between},${and}`)
      } return accept(column, 'nbtw')
    }
  }

  bindOperate(me)
  me.getParams = function() {
    const tmpTerms = me.buildParam(me.terms)
    const tmpSorts = me.buildSort(me.sorts)
    me.terms = []
    me.sorts = []
    return { ...tmpTerms, ...tmpSorts }
  }
  me.includes = function(columns) {
    me.param.includes = `${columns}`
    return me
  }
  me.excludes = function(columns) {
    me.param.excludes = `${columns}`
    return me
  }
  me.where = function(k, v, t) {
    me.and(k, v, t)
    return me
  }
  me.and = function(k, v, t) {
    me.nowType = 'and'
    if (k && v) { me.terms.push({ column: k, termType: t ? 'eq' : t, type: me.nowType, value: v }) }
    return me
  }
  me.orNest = function() {
    return me.nest(true, me)
  }
  me.nest = function(isOr, parent) {
    const nest = { type: isOr ? 'or' : 'and' }
    if (!parent) parent = me

    parent.terms.push(nest)
    nest.terms = []
    const fun = { terms: nest.terms, nowType: isOr ? 'or' : 'and' }

    bindOperate(fun)
    fun.and = function(k, v, t) {
      fun.nowType = 'and'
      if (k && v) { fun.terms.push({ column: k, termType: t ? 'eq' : t, value: v, type: 'and' }) }
      return fun
    }
    fun.or = function(k, v, t) {
      fun.nowType = 'or'
      if (k && v) { nest.terms.push({ column: k, termType: t ? 'eq' : t, value: v, type: 'or' }) }
      return fun
    }
    fun.exec = me.exec
    fun.orNest = function() {
      return me.orNest()
    }
    fun.nest = function() {
      return me.nest(false, nest)
    }
    fun.end = function() {
      return parent
    }
    return fun
  }
  me.or = function(k, v, t) {
    me.nowType = 'or'
    if (k && v) { me.terms.push({ column: k, termType: t ? 'eq' : t, value: v, type: me.nowType }) }
    return me
  }

  me.orderByDesc = function(f) {
    me.sorts.push({ name: f, order: 'desc' })
    return me
  }
  me.orderBy = function(f) {
    me.sorts.push({ name: f, order: 'asc' })
    return me
  }

  me.buildSort = (sorts) => {
    const tmp = {}
    sorts.forEach((e, i) => {
      for (const f in e) {
        if (f !== 'sorts') { tmp[`sorts[${i}].${f}`] = e[f] } else {
          const tmpTerms = me.buildSort(e[f])
          for (const f2 in tmpTerms) {
            tmp[`sorts[${i}].${f2}`] = tmpTerms[f2]
          }
        }
      }
    })
    return tmp
  }

  me.orderByAsc = function(f) {
    me.sorts.push({ name: f, order: 'asc' })
    return me
  }
  me.noPaging = function() {
    me.param.paging = 'false'
    return me
  }
  me.limit = function(pageIndex, pageSize) {
    me.param.pageIndex = pageIndex
    if (pageSize) {
      me.param.pageSize = pageSize
    }
    return me
  }

  me.buildParam = function(terms) {
    const tmp = {}
    terms.forEach((e, i) => {
      for (const f in e) {
        if (f !== 'terms') { tmp[`terms[${i}].${f}`] = e[f] } else {
          const tmpTerms = me.buildParam(e[f])
          for (const f2 in tmpTerms) {
            tmp[`terms[${i}].${f2}`] = tmpTerms[f2]
          }
        }
      }
    })
    return tmp
  }

  return me
}

export default DSLParameterBuilder()
