import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class EMS_Application {
    static int num;
    static int max = 10;

    static final String DB_URL = "jdbc:mysql://localhost:3306/EMS";
    static final String username = "root";
    static final String pass = System.getenv("DB_Pass");
    static Employee[] emp = new Employee[max];
    static Scanner sc = new Scanner(System.in);

    // Employee class
    static class Employee {
        String name;
        long code;
        String designation;
        int exp;
        int age;

        public Employee(String name, long code, String designation, int exp, int age) {
            this.name = name;
            this.code = code;
            this.designation = designation;
            this.exp = exp;
            this.age = age;
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Employee Management System 👋");
        System.out.println();

        // Performing MySQL operations
        mysqlOperations();
    }

    private static void mysqlOperations() {
        System.out.print("☑️ Select the options - \n1. Insert Record \n2. Delete Record \n3. Search in Records\n-> ");

        try {
            // Loading drivers
            Class.forName("com.mysql.cj.jdbc.Driver");
            int in = sc.nextInt();

            try (
                    Connection con = DriverManager.getConnection(DB_URL, username, pass);
                    Statement stm = con.createStatement();
            ) {
                switch (in) {
                    case 1:
                        inputRecords();
                        String sql = "INSERT INTO EMS_Record(Name, Code, Designation, Exp, Age) VALUES(?, ?, ?, ?, ?)";
                        PreparedStatement preStm = con.prepareStatement(sql);

                        for (int i = 1; i <= num; i++) {
                            if (emp[i] != null) {
                                preStm.setString(1, emp[i].name);
                                preStm.setLong(2, emp[i].code);
                                preStm.setString(3, emp[i].designation);
                                preStm.setInt(4, emp[i].exp);
                                preStm.setInt(5, emp[i].age);
                            } else {
                                break;
                            }
                        }
                        int rows = preStm.executeUpdate();

                        System.out.println();
                        if (rows > 0) System.out.println("Records inserted successfully! 🎉");
                        break;
                    case 2:
                        deleteRecord(con, stm);
                        break;
                    case 3:
                        System.out.print("Search by Employee\n1. Name\n2. Code\n-> ");
                        searchInTable(con, sc.nextInt());
                        break;
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: "+ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception: "+ex.getMessage());
        }
    }

    private static void deleteRecord(Connection con, Statement stm) throws SQLException {
        showDBTable(stm);

        System.out.println();
        System.out.print("Enter Id to delete the record: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM ems_record where id = ?";
        PreparedStatement preStm = con.prepareStatement(sql);
        preStm.setInt(1, id);

        int result = preStm.executeUpdate();

        if (result > 0) {
            System.out.println("☑️ Employee record deleted successfully");
        } else {
            System.out.println("❌ No record found with this ID: "+id);
        }
    }

    private static void searchInTable(Connection con, int option) throws SQLException {
        String sql;
        PreparedStatement preStm;
        ResultSet rs;
        boolean found;

        // Consuming new line
        sc.nextLine();
        switch (option) {
            case 1:
                System.out.print("Enter the employee name: ");
                sql = "Select * from ems_record where name = ?";
                preStm = con.prepareStatement(sql);
                String empName = sc.nextLine();
                preStm.setString(1, empName);

                rs = preStm.executeQuery();

                System.out.println();
                System.out.println("👨‍💼 Employee records -> ");
                found = false;
                while (rs.next()) {
                    found = true;
                    int Id = rs.getInt(1);
                    String name = rs.getString(2);
                    long code = rs.getLong(3);
                    String design = rs.getString(4);
                    int exp = rs.getInt(5);
                    int age = rs.getInt(6);

                    System.out.printf("ID: "+Id+" | Name: "+name+" | Code: "+code+" | Designation: "+design+" | Exp: "+exp+" | Age: "+age);
                    System.out.println();
                }

                if (!found) {
                    System.out.println();
                    System.out.println("❌ No records found with name "+empName);
                }

                rs.close();
                preStm.close();
                break;
            case 2:
                System.out.print("Enter the employee code: ");
                sql = "Select * from ems_record where code = ?";
                preStm = con.prepareStatement(sql);
                long empCode = sc.nextLong();
                preStm.setLong(1, empCode);

                rs = preStm.executeQuery();
                found = false;
                System.out.println("👨‍💼 Employee records ->");
                while (rs.next()) {
                    found = true;
                    int Id = rs.getInt(1);
                    String name = rs.getString(2);
                    long code = rs.getLong(3);
                    String design = rs.getString(4);
                    int exp = rs.getInt(5);
                    int age = rs.getInt(6);

                    System.out.printf("ID: "+Id+" | Name: "+name+" | Code: "+code+" | Designation: "+design+" | Exp: "+exp+" | Age: "+age);
                    System.out.println();
                }

                if (!found) {
                    System.out.println();
                    System.out.println("❌ No records found with code "+empCode);
                }

                rs.close();
                preStm.close();
                break;
            default:
                System.out.println("Please select the right option! ");
        }
    }

    private static void showDBTable(Statement stm) throws SQLException {
        String sql = "Select * from ems_record";
        ResultSet rs = stm.executeQuery(sql);

        System.out.println();
        System.out.println("👨‍💼 Employee records");
        while (rs.next()) {
            int Id = rs.getInt(1);
            String name = rs.getString(2);
            long code = rs.getLong(3);
            String design = rs.getString(4);
            int exp = rs.getInt(5);
            int age = rs.getInt(6);

            System.out.printf("ID: "+Id+" | Name: "+name+" | Code: "+code+" | Designation: "+design+" | Exp: "+exp+" | Age: "+age);
            System.out.println();
        }
        rs.close();
    }

    private static void inputRecords() {
        System.out.print("Enter the number of records - ");
        num = sc.nextInt();

        if (num > max) {
            System.out.println("Max you can create "+max+" records");
            return;
        }

        System.out.println();
        System.out.println("Enter table records!");
        for (int i = 0; i < num ; i++) {
            System.out.println("Enter the new employee record");

            // Clearing new line(/n) buffer
            sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Code: ");
            long code = sc.nextLong();

            sc.nextLine();

            System.out.print("Designation: ");
            String designation = sc.nextLine();

            System.out.print("Experience: ");
            int exp = sc.nextInt();

            System.out.print("Age: ");
            int age = sc.nextInt();

            emp[i] = new Employee(name, code, designation, exp, age);
            System.out.println();
        }

        showTable();
    }

    private static void showTable() {
        for (int i = 0; i < num; i++) {
            Employee empData = emp[i];
            System.out.println(i+1+" Employee data");
            System.out.println(
                    "Name: "+empData.name+
                            ", Code: "+empData.code+
                            ", Designation: "+empData.designation+
                            ", Exp: "+empData.exp+
                            ", Age: "+empData.age

            );
        }
    }
}
