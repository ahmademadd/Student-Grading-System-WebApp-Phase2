<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Controllers.Servlets.Dashboard" %>
<%@ page import="Models.Enrollment" %>
<%@ page import="Models.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            margin: 20px;
        }
        .container {
            width: 60%;
            margin: auto;
            background: white;
            padding: 20px;
            box-shadow: 0px 0px 10px gray;
            border-radius: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
        }
        th {
            background-color: #007BFF;
            color: white;
        }
        button {
            font-family: "Asap", sans-serif;
            cursor: pointer;
            color: #fff;
            font-size: 16px;
            text-transform: uppercase;
            width: 100%;
            border: 0;
            padding: 10px;
            margin-top: 20px;
            border-radius: 25px;
            background-color: #4ca1af;
            transition: background-color 300ms;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Welcome, <%= request.getAttribute("studentName") %>!</h2>
    <p>Email: <%= request.getAttribute("studentEmail") %></p>

    <h3>Your Courses and Grades</h3>
        <table border="1">
                <tr>
                    <th>Course</th>
                    <th>Your Grade</th>
                    <th>Course Average</th>
                    <th>Your Rank</th>
                </tr>
                <%
                    List<Enrollment> list = (List<Enrollment>) request.getAttribute("grades");
                    if (list != null) {
                        for (Enrollment enrollment : list) {
                %>
                <tr>
                    <td><%= enrollment.getCourse().getCourse_name() %></td>
                    <td><%= enrollment.getGrade() %></td>
                    <td><%= String.format("%.2f", enrollment.getCourseAvg()) %></td>
                    <td><%= enrollment.getStudentRank() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>

    <form action="logout" method="get">
        <button type="submit">Logout</button>
    </form>
</div>

</body>
</html>
