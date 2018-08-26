#基于Spring Aop的事物管理有如下两点缺陷
1.@Transactional 注解只应用到 public 方法
2.自调用问题：同一类中的其他没有@Transactional 注解的方法内部调用有@Transactional 注解的方法，有@Transactional 注解的方法的事务被忽略，不会发生回滚

#解决：可以改成基于AspectJ的事物管理，基于AspectJ的事物管理可以解决如上两个缺陷