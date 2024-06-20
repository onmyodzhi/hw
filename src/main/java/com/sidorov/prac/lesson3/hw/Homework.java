package com.sidorov.prac.lesson3.hw;

import com.sidorov.prac.lesson3.hw.models.Department;
import com.sidorov.prac.lesson3.hw.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Homework {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            createTableDepartment(connection);
            createTablePerson(connection);
            populateTables(connection);
            System.out.println(getPersonDepartmentName(connection, 1));
            System.out.println(getPersonDepartments(connection));
            System.out.println(getDepartmentPersons(connection));
        } catch (SQLException e) {
            System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTablePerson(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table person (
                      id bigint primary key,
                      name varchar(256),
                      age integer,
                      active boolean,
                      department_id bigint references department(id)
                    )
                    """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы Person произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    public static void createTableDepartment(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table department (
                    id bigint primary key,
                    name varchar(256)
                    )
                                                        """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы Department произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    private static void populateTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("insert into department (id, name) values (1, 'HR'), (2, 'Engineering')");
            statement.execute("insert into person (id, name, age, active, department_id) values (1, 'Alice', 30, true, 1), (2, 'Bob', 25, true, 2), (3, 'Charlie', 28, false, 1)");
        } catch (SQLException e) {
            System.err.println("Во время заполнения таблиц произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    /**
     * С помощью JDBC, выполнить следующие пункты:
     * 1. Создать таблицу Person (скопировать код с семниара)
     * 2. Создать таблицу Department (id bigint primary key, name varchar(128) not null)
     * 3. Добавить в таблицу Person поле department_id типа bigint (внешний ключ)
     * 4. Написать метод, который загружает Имя department по Идентификатору person
     * 5. * Написать метод, который загружает Map<String, String>, в которой маппинг person.name -> department.name
     *   Пример: [{"person #1", "department #1"}, {"person #2", "department #3}]
     * 6. ** Написать метод, который загружает Map<String, List<String>>, в которой маппинг department.name -> <person.name>
     *   Пример:
     *   [
     *     {"department #1", ["person #1", "person #2"]},
     *     {"department #2", ["person #3", "person #4"]}
     *   ]
     *
     *  7. *** Создать классы-обертки над таблицами, и в пунктах 4, 5, 6 возвращать объекты.
     */

    /**
     * Пункт 4
     */
    private static Department getPersonDepartmentName(Connection connection, long personId) throws SQLException {
        // я не могу вернуть только имя department так как они в теории могут повторяться,
        // так как поле не unique и поэтому место того чтобы делать 2 запроса я делаю 1
        // но возвращаю не только имя но и id чтобы было полное заполнение сущности department
        String sql = "select d.name, d.id from person p join department d on p.id = d.id where p.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return fillInDepartment(resultSet.getLong("id"),
                        resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Во время обработки запроса произошла ошибка: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Пункт 5
     */
    private static Map<Person, Department> getPersonDepartments(Connection connection) throws SQLException {
        Map<Person, Department> personDepartmentMap = new HashMap<>();

        String sql = "select p.name as person_name, p.id as person_id, " +
                "d.name as department_name, d.id as department_id " +
                " from person p join department d on p.department_id = d.id ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = fillInPerson(resultSet.getLong("person_id"),
                        resultSet.getString("person_name"));

                Department department = fillInDepartment(resultSet.getLong("department_id"),
                        resultSet.getString("department_name"));


                personDepartmentMap.put(person, department);
            }
        } catch (SQLException e) {
            System.err.println("Во время обработки запроса произошла ошибка: " + e.getMessage());
            throw e;
        }

        return personDepartmentMap;
    }

    /**
     * Пункт 6
     */
    private static Map<Department, List<Person>> getDepartmentPersons(Connection connection) throws SQLException {
        Map<Department, List<Person>> names = new HashMap<>();

        String slq = "select d.name as department_name, d.id as department_id, " +
                "p.name as person_name, p.id as person_id " +
                "from department d join person p on d.id = p.department_id ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(slq)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = fillInPerson(resultSet.getLong("person_id"),
                        resultSet.getString("person_name"));

                Department department = fillInDepartment(resultSet.getLong("department_id"),
                        resultSet.getString("department_name"));


                if (names.containsKey(department)) {
                    names.get(department).add(person);
                } else {
                    List<Person> persons = new ArrayList<>();

                    persons.add(person);
                    names.put(department, persons);
                }
            }
        } catch (SQLException e) {
            System.err.println("Во время обработки запроса произошла ошибка: " + e.getMessage());
            throw e;
        }

        return names;
    }

    private static Person fillInPerson(Long id, String name) {
        Person person = new Person();

        person.setId(id);
        person.setName(name);

        return person;
    }

    private static Department fillInDepartment(Long id, String name) {
        Department department = new Department();

        department.setId(id);
        department.setName(name);

        return department;
    }
}
