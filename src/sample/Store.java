package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Store {
    private static ArrayList<Book> addedBooks;

    public static ArrayList<Book> getAddedBooks() {
        return addedBooks;
    }

    private static ArrayList<User> getAllSigned(String identity) {
        ArrayList<User> temp = new ArrayList<>();
        String fileName;
        boolean isAdmin = identity.equals("admin");
        if (isAdmin) fileName = "signedAdmin";
        else fileName = "signedUser";
        if(!Paths.get(fileName).toFile().exists()) return temp;
        try {
            Files.readAllLines(Paths.get(fileName)).stream().map(line -> {
                ArrayList<String> each = new ArrayList<>();
                each.addAll(Arrays.asList(line.split(" ")));
                ArrayList<Book> hadBooks = new ArrayList<>();
                Iterator<String> iterable = each.iterator();
                boolean start = false;
                while (iterable.hasNext()){
                    String bookString = iterable.next();
                    if (bookString.equals("<Books>")) {
                        start = true;
                        continue;
                    }
                    if (bookString.equals("</Books>")) {
                        start = false;
                        continue;
                    }
                    if (start){
                        String[] i = bookString.split(",");
                        hadBooks.add(new Book(i[0],i[1],i[2],parseInt(i[3]), LocalDate.parse(i[4])));
                    }
                }
                if (isAdmin)
                    return new Admin(each.get(0),each.get(1),hadBooks);
                else return new User(each.get(0),each.get(1),hadBooks);
            }).forEach(temp::add);
        } catch (IOException e) {
            System.out.println("验证文件读取出错");
            e.printStackTrace();
        }
        return temp;
    }

    static String sign(String name,String pass,String identity) {
        String fileName;
        if (isSigned(name,identity)!=null)
            return "您已注册!请登陆";
        if (identity.equals("admin")) {
            fileName = "signedAdmin";
        } else {
            fileName = "signedUser";
        }
        try {
            FileWriter in = new FileWriter(fileName);
            StringBuilder sequence = new StringBuilder();
            sequence.append(name);
            sequence.append(" ");
            sequence.append(pass);
            in.append(sequence);
            in.close();
            return "注册成功";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "验证文件读取出错";
        } catch (IOException e) {
            e.printStackTrace();
            return "文件写入出错";
        }
    }

    public static User isSigned(String name, String identity) {
        ArrayList<User> got = getAllSigned(identity);
        return got.stream().filter(o -> o.name.equals(name)).findFirst().orElse(null);
    }

    static ArrayList<Book> importBooks() {
        addedBooks = new ArrayList<>();
        ArrayList<Book> allBooks = new ArrayList<>();
        if(!Paths.get("Books").toFile().exists()) {
            return allBooks;
        }
        try {
            Files.readAllLines(Paths.get("Books")).stream().map(line -> {
                String[] each = line.split(",");
                return new Book(each[0], each[1], each[2], parseInt(each[3]));
            }).forEach(allBooks::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allBooks;
    }

    static void saveBooks() {
        try {
            PrintWriter in = new PrintWriter("Books");
            //in.append(each); Attention:FileWriter.append不是追加内容，是流操作
            Controller.showAll.forEach(in::println);
            in.close();
        } catch (IOException e) {
            System.out.println("文件写入出错");
            e.printStackTrace();
        }
    }
}
