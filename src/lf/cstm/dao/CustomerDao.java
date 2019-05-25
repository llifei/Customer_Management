package lf.cstm.dao;


import cn.itcast.jdbc.TxQueryRunner;
import lf.cstm.domain.Customer;
import lf.cstm.domain.PageBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 持久层
 *
 * @author lifei
 */
public class CustomerDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * 添加客户
     *
     * @param c
     */
    public void add(Customer c) {
        String sql = "INSERT INTO t_customer VALUES(?,?,?,?,?,?,?)";
        Object[] params = {c.getCid(), c.getCname(), c.getGender(),
                c.getBirthday(), c.getCellphone(), c.getEmail(), c.getDescription()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询所有
     * @return
     */
    public PageBean<Customer> findAll(int pc,int ps){
        try {
            /**
             * 1.创建PageBean对象
             * 2.设置pb的pc和ps
             * 3.得到tr,设置给pb
             * 4.得到beanList，设置给pb
             * 5.设置url
             * 6.返回pb
             */
            PageBean<Customer> pb = new PageBean<Customer>();
            pb.setPc(pc);
            pb.setPs(ps);
            /**
             * 得到tr
             */
            String sql="SELECT COUNT(*) FROM t_customer";
            Number num= (Number) qr.query(sql,new ScalarHandler());
            int tr=num.intValue();
            pb.setTr(tr);
            /**
             * 得到beanList
             */
            sql="SELECT * FROM t_customer LIMIT ?,?";
            Object[]params={(pc-1)*ps,ps};
            List<Customer>beanList=qr.query(sql,
                    new BeanListHandler<Customer>(Customer.class),params);
            pb.setBeanList(beanList);
            return pb;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 指定查询
     * @param cid
     * @return
     */
    public Customer findByCid(String cid){
        String sql="SELECT * FROM t_customer WHERE cid=?";
        try {
            return qr.query(sql, new BeanHandler<Customer>(Customer.class),cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 编辑客户
     * @param customer
     */
    public void edit(Customer customer) {
        String sql="UPDATE t_customer SET cname=?,gender=?,birthday=?,"
                    +"cellphone=?,email=?,description=? WHERE cid=?";
        Object[]params={customer.getCname(), customer.getGender(), customer.getBirthday(),
                customer.getCellphone(), customer.getEmail(), customer.getDescription(),customer.getCid(),};
        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String cid) {
        String sql="DELETE FROM t_customer WHERE cid=?";
        String param=cid;
        try {
            qr.update(sql,param);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public PageBean<Customer> query(Customer criteria,int pc,int ps) {
//        PageBean<Customer>beanList=new PageBean<>();
//        try {
//            /**
//             * 一、给出sql模板
//             * 二、给出参数
//             */
//            /**
//             * 1.给出一个sql语句前半部分
//             */
//            StringBuilder sql = new StringBuilder("SELECT * FROM t_customer WHERE 1=1"); //WHERE cname=?,gender=?,cellphone=?,email=?";
//            /**
//             * 2.获取条件参数
//             */
//            String cname = criteria.getCname();
//            String gender = criteria.getGender();
//            String cellphone = criteria.getCellphone();
//            String email = criteria.getEmail();
//            /**
//             * 3.判断条件，完成向sql中追加where子句
//             * 4.创建一个ArrayList，用来装载参数值
//             */
//            List<Object> params = new ArrayList<Object>();
//            if (cname != null && !cname.trim().isEmpty()) {
//                sql.append(" AND cname LIKE ?");
//                params.add("%"+cname+"%");
//            }
//            if (gender != null && !gender.trim().isEmpty()) {
//                sql.append(" AND gender=?");
//                params.add(gender);
//            }
//            if (cellphone != null && !cellphone.trim().isEmpty()) {
//                sql.append(" AND cellphone LIKE ?");
//                params.add("%"+cellphone+"%");
//            }
//            if (email != null && !email.trim().isEmpty()) {
//                sql.append(" AND email LIKE ?");
//                params.add("%"+email+"%");
//            }
//            /**
//             * 三、执行query
//             */
//            beanList.setBeanList(qr.query(sql.toString(), new BeanListHandler<Customer>(Customer.class), params.toArray()));
//
//            return beanList;
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//    }

    public PageBean<Customer> query(Customer criteria,int pc,int ps) {
    try {
        /**
         * 1.创建PageBean对象
         * 2.设置已有的属性，pc和ps
         * 3.得到tr
         * 4.得到beanList
         */
        //创建pb，设置已有属性
        PageBean<Customer>pb=new PageBean<>();
        //得到tr
        /**
         * 1.给出一个sql语句前半部分
         */
        StringBuilder cntSql = new StringBuilder("SELECT COUNT(*) FROM t_customer");
        StringBuilder whereSql=new StringBuilder(" WHERE 1=1");
        /**
         * 2.获取条件参数
         */
        String cname = criteria.getCname();
        String gender = criteria.getGender();
        String cellphone = criteria.getCellphone();
        String email = criteria.getEmail();
        /**
         * 3.判断条件，完成向sql中追加where子句
         * 4.创建一个ArrayList，用来装载参数值
         */
        List<Object> params = new ArrayList<Object>();
        if (cname != null && !cname.trim().isEmpty()) {
            whereSql.append(" AND cname LIKE ?");
            params.add("%"+cname+"%");
        }
        if (gender != null && !gender.trim().isEmpty()) {
            whereSql.append(" AND gender=?");
            params.add(gender);
        }
        if (cellphone != null && !cellphone.trim().isEmpty()) {
            whereSql.append(" AND cellphone LIKE ?");
            params.add("%"+cellphone+"%");
        }
        if (email != null && !email.trim().isEmpty()) {
            whereSql.append(" AND email LIKE ?");
            params.add("%"+email+"%");
        }
        /**
         * 执行
         */
        Number num=(Number)qr.query(cntSql.append(whereSql).toString()
                    ,new ScalarHandler(),params.toArray());
        int tr=num.intValue();
        pb.setTr(tr);
        /**
         * 得到beanList
         */
        StringBuilder sql=new StringBuilder("SELECT * FROM t_customer");
        StringBuilder limitSql=new StringBuilder(" LIMIT ?,?");
        params.add((pc-1)*ps);
        params.add(ps);
        /**
         * 执行
         */
        List<Customer>beanList=qr.query(sql.append(whereSql).append(limitSql).toString(),
                new BeanListHandler<Customer>(Customer.class),params.toArray());
        pb.setBeanList(beanList);

        return pb;
    }catch (SQLException e){
        throw new RuntimeException(e);
    }
}
}
