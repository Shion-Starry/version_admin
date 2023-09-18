package app.desty.chat_admin.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选择规则工具类，主要用于处理各种选择器的单选互斥的规则管理
 */
public class SelectRuleUtil<X> {

    private final List<Rule<X>> ruleList = new ArrayList<>();

    /**
     * 添加选择规则
     * @param rule 规则接口实现
     * @return this
     */
    public SelectRuleUtil<X> addRule(Rule<X> rule) {
        ruleList.add(rule);
        return this;
    }

    /**
     * 经过所有规则过滤后，x将会被添加到list
     * @param x 选择的元素
     * @param list 当前已选中的元素列表
     */
    public void select(X x, List<X> list) {
        for (Rule<X> xRule : ruleList) {
            xRule.execRule(x, list);
        }
        list.add(x);
    }

    interface Rule<T> {
        void execRule(T select, List<T> list);
    }

    public static class MutexRule<R> implements Rule<R> {
        private final List<R> listA;
        private final List<R> listB;

        /**
         * 规则实现：集合互斥，A集合直接无限制，但跟B集合互斥，一旦选择B元素会取消所有A中元素
         * @param listA AB等价，相互互斥
         * @param listB AB等价，相互互斥
         */
        public MutexRule(List<R> listA, List<R> listB) {
            this.listA = listA;
            this.listB = listB;
        }
        /**
         * 规则实现：集合互斥，A集合直接无限制，但跟B集合互斥，一旦选择B元素会取消所有A中元素
         * 无参构造器，请继续调用addList方法添加集合元素
         */
        public MutexRule() {
            listA = new ArrayList<>();
            listB = new ArrayList<>();
        }

        @SafeVarargs
        public final MutexRule<R> addListA(R... rs) {
            listA.addAll(Arrays.asList(rs));
            return this;
        }

        @SafeVarargs
        public final MutexRule<R> addListB(R... rs) {
            listB.addAll(Arrays.asList(rs));
            return this;
        }

        public final MutexRule<R> addListB(List<R> listB) {
            this.listB.addAll(listB);
            return this;
        }

        @Override
        public void execRule(R select, List<R> list) {
            if (listA.contains(select)) {
                for (R r : listB) {
                    list.remove(r);
                }
            } else if (listB.contains(select)) {
                for (R r : listA) {
                    list.remove(r);
                }
            }
        }
    }

    public static class RemoveAllRule<R> implements Rule<R> {
        public R r;

        /**
         * 规则实现：选择特定选项移除其他所有
         * 构造是需传入指定选项
         */
        public RemoveAllRule(R r) {
            this.r = r;
        }

        @Override
        public void execRule(R select, List<R> list) {
            if (r != null && r.equals(select)) {
                list.clear();
            }
        }
    }


    public static class SingleRule<R> implements Rule<R> {
        private final List<R> singleList;

        /**
         * 规则实现：单选， list构造器
         * @param singleList 单选范围
         */
        public SingleRule(List<R> singleList) {
            this.singleList = singleList;
        }

        /**
         * 规则实现：单选， 不定参构造器
         * @param rs 单选范围
         */
        @SafeVarargs
        public SingleRule(R... rs) {
            singleList = new ArrayList<>(rs.length);
            singleList.addAll(Arrays.asList(rs));
        }


        @Override
        public void execRule(R select, List<R> list) {
            if (singleList.contains(select)) {
                for (R r : singleList) {
                    if (r.equals(select)) continue;
                    list.remove(r);
                }
            }
        }
    }


}
