package lf.cstm.dao;

import cn.itcast.commons.CommonUtils;
import lf.cstm.domain.Customer;
import org.junit.Test;

public class CustomerTest {
    @Test
    public void fun1(){
        CustomerDao dao=new CustomerDao();
        for(int i=1;i<=300;i++){
            Customer c=new Customer();

            c.setCid(CommonUtils.uuid());
            c.setCname("cstm_"+i);
            c.setBirthday("2019-5-16");
            c.setGender(i%2==0?"男":"女");
            c.setCellphone("155225"+i);
            c.setEmail("cstm_"+i+"@163.com");
            c.setDescription("我是客户");

            dao.add(c);
        }
    }
}
