package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.IntFunction;

import static java.lang.Integer.parseInt;

public class Store {
    private static ArrayList<Book> addedBooks;
    static ArrayList<Integer> bookIndex;

    public static ArrayList<Book> getAddedBooks() {
        return addedBooks;
    }

//    private static ArrayList<User> getAllSigned(String identity) {
//        ArrayList<User> temp = new ArrayList<>();
//        String fileName;
//        boolean isAdmin = identity.equals("admin");
//        if (isAdmin) fileName = "signedAdmin";
//        else fileName = "signedUser";
//        if(!Paths.get(fileName).toFile().exists()) return temp;
//        try {
//            Files.readAllLines(Paths.get(fileName)).stream().map(line -> {
//                ArrayList<String> each = new ArrayList<>();
//                each.addAll(Arrays.asList(line.split(" ")));
//                ArrayList<Book> hadBooks = new ArrayList<>();
//                Iterator<String> iterable = each.iterator();
//                boolean start = false;
//                while (iterable.hasNext()){
//                    String bookString = iterable.next();
//                    if (bookString.equals("<Books>")) {
//                        start = true;
//                        continue;
//                    }
//                    if (bookString.equals("</Books>")) {
//                        start = false;
//                        continue;
//                    }
//                    if (start){
//                        String[] i = bookString.split(",");
//                        hadBooks.add(new Book(i[0],i[1],i[2],parseInt(i[3]), LocalDate.parse(i[4])));
//                    }
//                }
//                if (isAdmin)
//                    return new Admin(each.get(0),each.get(1),hadBooks);
//                else return new User(each.get(0),each.get(1),hadBooks);
//            }).forEach(temp::add);
//        } catch (IOException e) {
//            System.out.println("验证文件读取出错");
//            e.printStackTrace();
//        }
//        return temp;
//    }

    static String signSQL(String name,String pass,String identity){
        if (SQL.select("SELECT name,password FROM " + identity + " WHERE name=\""+name+"\" AND password=\""+pass+"\"").get("name").size() > 1) return "您已注册!请登陆";
        else {
            SQL.update("INSERT INTO "+identity+" (name,password) VALUES (?,?)",new Object[]{name,pass});
            return "注册成功";
        }
    }

//    static String sign(String name,String pass,String identity) {
//        String fileName;
//        if (isSigned(name,identity)!=null)
//            return "您已注册!请登陆";
//        if (identity.equals("admin")) {
//            fileName = "signedAdmin";
//        } else {
//            fileName = "signedUser";
//        }
//        try {
//            FileWriter in = new FileWriter(fileName);
//            StringBuilder sequence = new StringBuilder();
//            sequence.append(name);
//            sequence.append(" ");
//            sequence.append(pass);
//            in.append(sequence);
//            in.close();
//            return "注册成功";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "验证文件读取出错";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "文件写入出错";
//        }
//    }

//    public static User isSigned(String name, String identity) {
//        ArrayList<User> got = getAllSigned(identity);
//        return got.stream().filter(o -> o.name.equals(name)).findFirst().orElse(null);
//    }

    public static User isSignedSQL(String name, String identity){
        HashMap<String,ArrayList<String>> got = SQL.select("SELECT id,name,password,books FROM "+identity+" WHERE name=\""+name+"\"");
        if (got.get("name").size() < 2) return null;
        else {
            if (identity.equals("admin")) return new Admin(parseInt(got.get("id").get(1)),got.get("name").get(1),got.get("password").get(1),parseBook(got.get("books").get(1)));
            else return new User(parseInt(got.get("id").get(1)),got.get("name").get(1),got.get("password").get(1),parseBook(got.get("books").get(1)));
        }
    }

    private static ArrayList<Book> parseBook(String rec){
        System.out.println(rec);
        ArrayList<Book> hadBooks = new ArrayList<>();
        if (rec==null) return hadBooks;
        Scanner sc = new Scanner(rec);
        while (sc.hasNext()) {
            String[] i = sc.next().split(",");
            hadBooks.add(new Book(parseInt(i[0]),i[1], i[2], i[3], parseInt(i[4]), LocalDate.parse(i[5])));
        }
        return hadBooks;
    }

    static ArrayList<Book> importBooksSQL(){
        addedBooks = new ArrayList<>();
        bookIndex = new ArrayList<>();
        ArrayList<Book> allBooks = new ArrayList<>();
        HashMap<String,ArrayList<String>> res = SQL.select("SELECT id,name,author,publish,number FROM books");
        ArrayList<String> id = res.get("id");
        ArrayList<String> name = res.get("name");
        ArrayList<String> author = res.get("author");
        ArrayList<String> publish = res.get("publish");
        ArrayList<String> number = res.get("number");
        int len = name.size();
        for (int i = 1; i < len; i++) {
            int index = parseInt(id.get(i));
            allBooks.add(new Book(index,name.get(i),author.get(i),publish.get(i),parseInt(number.get(i))));
            bookIndex.add(index);
        }
        return allBooks;
    }

//    static ArrayList<Book> importBooks() {
//        addedBooks = new ArrayList<>();
//        ArrayList<Book> allBooks = new ArrayList<>();
//        if(!Paths.get("Books").toFile().exists()) {
//            return allBooks;
//        }
//        try {
//            Files.readAllLines(Paths.get("Books")).stream().map(line -> {
//                String[] each = line.split(",");
//                return new Book(each[0], each[1], each[2], parseInt(each[3]));
//            }).forEach(allBooks::add);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return allBooks;
//    }

    static void saveBooksSQL(){
        for (Book book : Controller.showAll) {
            SQL.update("REPLACE INTO books(id,name,author,publish,number) VALUES (?,?,?,?,?)",new Object[]{book.id,book.name.get(),book.author.get(),book.publish.get(),book.number.get()});
        }
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
