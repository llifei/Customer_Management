package lf.cstm.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import lf.cstm.domain.Customer;
import lf.cstm.domain.PageBean;
import lf.cstm.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *web层
 * @author lifei
 */
@WebServlet("/CustomerServlet")
public class CustomerServlet extends BaseServlet {
    private CustomerService customerService=new CustomerService();

    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1.封装表单数据到Customer对象中
         * 2.补全：cid，使用uuid
         * 3.使用service方法完成添加工作
         * 4.向request域中保存成功信息
         * 5.转发到msg.jsp
         */
        Customer c= CommonUtils.toBean(request.getParameterMap(),Customer.class);
        c.setCid(CommonUtils.uuid());
        customerService.add(c);
        request.setAttribute("msg","恭喜，添加客户成功！");
        return "f:/msg.jsp";
    }

    /**
     * 查询所有
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
//    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        /**
//         * 1.调用service查询所有客户
//         * 2.保存到request域
//         * 3.转发到list.jsp
//         */
//        request.setAttribute("cstmList",customerService.findAll());
//        return "f:/list.jsp";
//    }
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1.获取页面传递的pc
         * 2.给定ps的值
         * 3.使用pc和ps调用service方法，得到PageBean，保存到request域
         */
        int pc=getPc(request);//得到pc
        int ps=10;//给定ps的值
        PageBean<Customer> pb= (PageBean<Customer>) customerService.findAll(pc,ps);
        request.setAttribute("pb",pb);
        return "f:/list.jsp";
    }
    public int getPc(HttpServletRequest request){
        String value=request.getParameter("pc");
        if(value==null||value.trim().isEmpty()){
            return 1;
        }
        return Integer.parseInt(value);
    }

    /**
     * 编辑之前的查询工作
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String preEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1.获取参数cid
         * 2.调用service的查询方法，通过cid查询Customer对象
         * 3.保存到request域
         * 4.转发到edit.jsp
         */
        String cid=request.getParameter("cid");
        Customer c=customerService.load(cid);
        request.setAttribute("cstm",c);
        return "f:/edit.jsp";
    }

    /**
     *编辑方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1.封装表单数据到Customer对象中
         * 2.调用service方法完成修改
         * 3.保存成功信息到request域中
         * 4.转发msg.jsp显示成功信息
         */
        //已经封装了cid到Customer对象中
        Customer customer=CommonUtils.toBean(request.getParameterMap(),Customer.class);
        customerService.edit(customer);
        request.setAttribute("msg","恭喜，编辑客户成功！");
        return "f:/msg.jsp";
    }

    /**
     * 删除客户
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        customerService.delete(cid);
        request.setAttribute("msg","删除客户成功！");
        return"f:/msg.jsp";
    }


    public String query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1.封装表单数据到Customer对象中，它只有四个属性(cname,gender,cellphone,email)
         *   它就是一个条件
         * 2.得到pc
         * 3.给定ps
         * 4.使用pc和ps，以及条件对象，调用service方法，得到List<Customer>
         * 3.把PageBean保存到request域中
         * 4.转发到list.jsp
         */
        //获取查询条件
        Customer criteria=CommonUtils.toBean(request.getParameterMap(),Customer.class);
        //处理GET请求方式编码问题
        criteria=encoding(criteria);
        int pc=getPc(request);
        int ps=10;//给定ps的值，每页10行记录
        PageBean<Customer> pb=customerService.query(criteria,pc,ps);

        //得到url，保存到pb中
        pb.setUrl(getUrl(request));

        request.setAttribute("pb",pb);
        return "/list.jsp";
    }

    private Customer encoding(Customer criteria) throws UnsupportedEncodingException {
        String cname=criteria.getCname();
        String gender=criteria.getGender();
        String cellphone=criteria.getCellphone();
        String email=criteria.getEmail();

        if(cname!=null&&!cname.trim().isEmpty()) {
            cname = new String(cname.getBytes("ISO-8859-1"), "utf-8");
            criteria.setCname(cname);
        }
        if(gender!=null&&!gender.trim().isEmpty()) {
            gender = new String(gender.getBytes("ISO-8859-1"), "utf-8");
            criteria.setCname(gender);
        }if(cellphone!=null&&!cellphone.trim().isEmpty()) {
            cellphone = new String(cellphone.getBytes("ISO-8859-1"), "utf-8");
            criteria.setCname(cellphone);
        }if(email!=null&&!email.trim().isEmpty()) {
            email = new String(email.getBytes("ISO-8859-1"), "utf-8");
            criteria.setCname(email);
        }
        return criteria;
    }

    public String getUrl(HttpServletRequest request){
        String contextPath=request.getContextPath();//获取项目名
        String servletPath=request.getServletPath();//获取servletPath，即/CustomerServlet
        String queryString=request.getQueryString();//获取问号之后的参数部分

//        判断参数部分是否包含pc这个参数，如果包含，截取去掉它
        if(queryString.contains("&pc=")){
            int index=queryString.lastIndexOf("&pc=");
            queryString=queryString.substring(0,index);
        }
        return contextPath+servletPath+"?"+queryString;
    }
}
