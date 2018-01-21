# 图书馆管理系统

1. 未完成部分:

- [ ] 申请延期
- [ ] 查看详情
- [ ] 个人签名

2. 个人觉得有意义的部分
   - “记住密码”功能
   - 讲展示tip变成一个独立的工具类
   - 使用TreeTable，其归档作用用在这里十分合适

3. 学到的东西
   - RandomAcessFile 就是C的文件指针，超好用，可以同时读写还可以跳，除了无法实现删除，定位修改什么的都十分容易
   - JavaFx的定位系统Point2D
   - JavaFX的控件TreeTable，DatePicker

4. 部分展示
   - 头像显示不正常，因为资源的路径没办法正确编译进去

5. 数据存储部分
   - 数据文件并未加密
   - signedAdmin 存放注册管理员相关
   - signedUser 存放注册用户相关
   - Books 存放所有书籍

6. 改为数据库后

   所有与文件操作相关的函数均加上SQL后缀变为与数据库交互的操作

   从数据库中取数据是以HashMap\<String,ArrayList\<String\>\>的形式取出，方便后续处理

   核心代码：

   ```java
   stmt = conn.createStatement();
   ResultSet rs = stmt.executeQuery(sql);
   ResultSetMetaData data = rs.getMetaData();
   res = new HashMap<>();
   for (int i = 1; i <= data.getColumnCount(); i++) {
      ArrayList<String> e = new ArrayList<>();
      e.add(data.getColumnClassName(i));
      res.put(data.getColumnName(i),e);
   }
   while (rs.next()){
      for (int i = 1; i <= data.getColumnCount(); i++) {
          res.get(data.getColumnName(i)).add(rs.getString(data.getColumnName(i)));
      }
   }
   ```

   ​

