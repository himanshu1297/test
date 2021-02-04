/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.himanshu.csvread;

import com.himanshu.csvread.config.HibernateUtil;
import com.himanshu.csvread.model.address;
import com.himanshu.csvread.model.record;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Himanshu
 */
public class uploadform extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        File file_path = null;
        String file_name = null;
        String PATH = "E://Himanshu//";
		
        boolean iscsv = false;
        String res = null;
        if (isMultipart) {
            try {
                PrintWriter out = response.getWriter();
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);
                Iterator iter = items.iterator();
                if (iter.hasNext()) {
                    FileItem item;
                    do {
                        item = (FileItem) iter.next();
                        String field_name = item.getFieldName();
                        if (!item.isFormField()) {
                            if (field_name.equals("file")) {
                                file_name = item.getName();
                                if (!file_name.isEmpty()) {
                                    if (file_name.toLowerCase().endsWith(".csv")) {
                                        iscsv = true;
                                    } else {
                                        iscsv = false;
                                    }
                                    if (!iscsv) {
                                        res = "Allowed file formats are .csv";
                                        out.print(res);
                                        return;
                                    }
                                    if (file_name != null && iscsv) {
                                        String fullpath = PATH + File.separator + "myfile";
                                        file_path = new File(fullpath);
                                        if (file_path != null && !file_path.exists()) {
                                            boolean b = file_path.mkdir();
                                            file_path = new File(file_path,
                                                    getNewFileName("myfile", item.getName()));
                                            item.write(file_path);
                                        } else {
                                            file_path = new File(file_path,
                                                    getNewFileName("myfile", item.getName()));
                                            if (file_path.exists() && file_path.isFile()) {
                                                file_path.delete();
                                            }
                                            item.write(file_path);
                                        }
                                    }
                                }
                            }
                        }
                    } while (iter.hasNext());
                }
                if (file_path.exists() && file_path.isFile()) {
                    Path p = Paths.get(PATH + "//myfile//myfile.csv");
                    int name_index = -1, last_index = 0, birth_index = 0, age_index = 0, addres_index = 0, addres_index1 = 0, city_index = 0, state_index = 0, country_index = 0, postal_index = 0;
                    List<String> l = Files.readAllLines(p);
                    int i = 0;
                    String header = "";
                    List<String> l1 = new ArrayList<String>();
                    for (String s : l) {
                        if (i == 0) {
                            header = s;
                            i++;
                        } else {
                            l1.add(s);
                        }
                    }
                    String[] h = header.split(",");
                    for (int j = 0; j < h.length; j++) {
                        if (h[j].equalsIgnoreCase("First name")) {
                            name_index = j;
                        } else if (h[j].equalsIgnoreCase("Last name")) {
                            last_index = j;
                        } else if (h[j].equalsIgnoreCase("Date of birth")) {
                            birth_index = j;
                        } else if (h[j].equalsIgnoreCase("Age")) {
                            age_index = j;
                        } else if (h[j].equalsIgnoreCase("Address Line1")) {
                            addres_index = j;
                        } else if (h[j].equalsIgnoreCase("Address Line2")) {
                            addres_index1 = j;
                        } else if (h[j].equalsIgnoreCase("City")) {
                            city_index = j;
                        } else if (h[j].equalsIgnoreCase("State")) {
                            state_index = j;
                        } else if (h[j].equalsIgnoreCase("Country")) {
                            country_index = j;
                        } else if (h[j].equalsIgnoreCase("Postal Code")) {
                            postal_index = j;
                        }
                    }
                    String charragex = "^[a-zA-Z]*$";
                    String date_ragex = "^(0?[1-9]|1[0-2])\\/(0?[1-9]|1\\d|2\\d|3[01])\\/(19|20)\\d{2}$";
                    int line_number = 1;
                    if (name_index == -1 || last_index == 0 || birth_index == 0 || age_index == 0 || addres_index == 0 || addres_index1 == 0 || city_index == 0 || state_index == 0 || country_index == 0 || postal_index == 0) {
                        out.print("one of the header field is missing");
                    } else {
                        for (String list : l1) {
                            line_number++;
                            String[] ll = list.split(",");
                            for (int k = 0; k < ll.length; k++) {
                                if (k == name_index) {
                                    if (!ll[k].matches(charragex)) {
                                        out.println("first name field should only contain character at line: " + line_number);
                                        return;
                                    }
                                    if (ll[k].length() > 20) {
                                        out.println("first name field length should not greater then 20 at line: " + line_number);
                                        return;
                                    }
                                } else if (k == last_index) {
                                    if (!ll[k].matches(charragex)) {
                                        out.println("last name field should only contain character at line: " + line_number);
                                        return;
                                    }
                                    if (ll[k].length() > 20) {
                                        out.println("last name field length should not greater then 20 at line: " + line_number);
                                        return;
                                    }
                                } else if (k == birth_index) {
                                    if (ll[k].equals("")) {
                                        out.println("age field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (!ll[k].matches(date_ragex)) {
                                        out.println("date should be in MM/dd/YYYY format at line: " + line_number);
                                        return;
                                    }
                                } else if (k == age_index) {
                                    if (ll[k].equals("")) {
                                        out.println("age field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (!ll[k].matches("^[0-9]*$")) {
                                        out.println("age field should only contain digit at line: " + line_number);
                                        return;
                                    }
                                    if (Integer.parseInt(ll[k]) < 24 || Integer.parseInt(ll[k]) > 41) {
                                        out.println("age should be in range 24 and 40 at line " + line_number);
                                        return;
                                    }
                                } else if (k == addres_index) {
                                    if (ll[k].equals("")) {
                                        out.println("address line 1 field should not be empty at line: " + line_number);
                                        return;
                                    }
                                } else if (k == addres_index1) {
                                    if (ll[k].equals("")) {
                                        out.println("address line 2 field should not be empty at line: " + line_number);
                                        return;
                                    }
                                } else if (k == city_index) {
                                    if (ll[k].equals("")) {
                                        out.println("city field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (!ll[k].matches(charragex)) {
                                        out.println("city field should only contain character at line: " + line_number);
                                        return;
                                    }
                                } else if (k == state_index) {
                                    if (ll[k].equals("")) {
                                        out.println("state field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (!ll[k].matches(charragex)) {
                                        out.println("state field should only contain character at line: " + line_number);
                                        return;
                                    }
                                } else if (k == country_index) {
                                    if (ll[k].equals("")) {
                                        out.println("country field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (!ll[k].matches(charragex)) {
                                        out.println("country field should only contain character at line: " + line_number);
                                        return;
                                    }
                                } else if (k == postal_index) {
                                    if (ll[k].equals("")) {
                                        out.println("postal code field should not be empty at line: " + line_number);
                                        return;
                                    }
                                    if (ll[k].length() != 6) {
                                        out.println("postal code field length should be 6 at line: " + line_number);
                                        return;
                                    }
                                }
                            }
                        }
                        Session s = HibernateUtil.getSessionFactory().openSession();
                        s.beginTransaction();
                        int num = 0;
                        for (String s1 : l) {
                            if (num == 0) {
                                num++;
                            } else {
                                String[] ll = s1.split(",");
                                record r = new record();
                                address a = new address();
                                for (int k = 0; k < ll.length; k++) {
                                    r.setFname(ll[name_index]);
                                    r.setLname(ll[last_index]);
                                    int age = Integer.parseInt(ll[age_index]);
                                    r.setAge(age);
                                    r.setBirthdate(ll[birth_index]);
                                    a.setAddress_line1(ll[addres_index]);
                                    a.setAddress_line2(ll[addres_index1]);
                                    a.setCity(ll[city_index]);
                                    a.setCountry(ll[country_index]);
                                    a.setState(ll[state_index]);
                                    a.setPostalcode(ll[postal_index]);
                                    r.setAddress(a);
                                    a.setR(r);
                                }
                                s.persist(r);
                            }
                        }
                        List<record> list = getRecord(s);
                        if (list.size() > 0) {
                            out.println("<table" + "	class=\"table table-bordered table-hover table-responsive\"\r\n"
                                    + " id=\"Mytable\"	style=\"margin-left: .5in; width: 90%\">");
                            out.print("<thead>\r\n<tr>\r\n");
                            for (String head : h) {
                                out.print("<th>" + head + "</th>\r\n");
                            }
                            out.print("</tr>\r\n" + "</thead>\r\n" + "<tbody>");
                            for (record r : list) {
                                out.println("<tr>");
                                out.println("<td> " + r.getFname() + "</td>\r\n");
                                out.println("<td> " + r.getLname() + "</td>\r\n");
                                out.println("<td>" + r.getBirthdate() + "</td>\r\n");
                                out.println("<td> " + r.getAge() + "</td>\r\n");
                                address add = r.getAddress();
                                out.println("<td> " + add.getAddress_line1() + "</td>\r\n");
                                out.println("<td>" + add.getAddress_line2() + "</td>\r\n");
                                out.println("<td> " + add.getCity() + "</td>\r\n");
                                out.println("<td>" + add.getState() + "</td>\r\n");
                                out.println("<td> " + add.getCountry() + "</td>\r\n");
                                out.println("<td>" + add.getPostalcode() + "</td>\r\n");
                                out.println("</tr>");
                            }
                            out.print("</tbody>\r\n" + "</table><p\r\n"
                                    + "	style='margin: 0in; margin-bottom: .0001pt; font-size: 16px; font-family: \"Calibri\", sans-serif; margin-left: .5in; background: white;'>\r\n"
                                    + "	<span\r\n"
                                    + "		style='font-family: \"Arial\", sans-serif; color: black;'>&nbsp;</span>\r\n"
                                    + "</p>");
                        }
                        s.getTransaction().commit();
                        s.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getNewFileName(String new_file_name, String old_filename) {
        int i = old_filename.lastIndexOf(".");
        String ext = old_filename.substring(i);
        new_file_name = new_file_name + ext;
        return new_file_name;
    }

    private List<record> getRecord(Session s) {
        Query query = s.createQuery("from record").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<record> list = query.getResultList();
        return list;
    }
}
