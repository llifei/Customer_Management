package lf.cstm.service;

import lf.cstm.dao.CustomerDao;
import lf.cstm.domain.Customer;
import lf.cstm.domain.PageBean;

import java.util.List;

/**
 * 业务层
 * @author lifei
 */
public class CustomerService {
    private CustomerDao customerDao=new CustomerDao();

    /**
     * 添加客户
     * @param c
     */
    public void add(Customer c){
        customerDao.add(c);
    }

    /**
     * 查询所有
     * @return
     */
    public PageBean<Customer> findAll(int pc,int ps){
        return customerDao.findAll(pc,ps);
    }

    /**
     * 加载客户
     * @param cid
     * @return
     */
    public Customer load(String cid){
        return customerDao.findByCid(cid);
    }

    public void edit(Customer customer) {
        customerDao.edit(customer);
    }

    public void delete(String cid) {
        customerDao.delete(cid);
    }

    public PageBean<Customer> query(Customer criteria,int pc,int ps) {
        return customerDao.query(criteria,pc,ps);
    }
}
