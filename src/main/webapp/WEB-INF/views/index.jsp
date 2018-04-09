<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=${encoding}" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
    <title>TODO-лист</title>
    <link href="./static/css/bootstrap.css" rel="stylesheet" />
    <link href="./static/css/styles.css" rel="stylesheet" />
    <script type="text/javascript" src="./static/js/jquery.js"></script>
    <script type="text/javascript" src="./static/js/core.js"></script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <h1 class="text-center">Time tracker<a class="btn btn-primary ml-1 d-none  js-btn-logout"  role="button" href="${indexRef}">Выйти</a></h1>
                <form class="mb-3 form-auth js-form-auth" method="post" action="${loginRef}">
                    <table class="mx-auto">
                        <tr>
                            <td>
                                <input class="form-control js-field-login" type="text" name="login" required="required" placeholder="Логин" />
                            </td>
                            <td>
                                <input class="form-control js-field-pass" type="password" name="pass" required="required" placeholder="Пароль" />
                            </td>
                            <td>
                                <input class="btn btn-primary js-form-auth-submit" type="submit" value="Войти" />
                            </td>
                        </tr>
                    </table>
                </form>
                <table class="mb-3 mx-auto js-tracker-actions d-none">
                    <tr>
                        <td>
                            <a class="btn btn-primary js-tracker-action" data-action="resume" role="button" href="#">Пришёл</a>
                        </td>
                        <td>
                            <a class="btn btn-primary js-tracker-action" data-action="wait" role="button" href="#">Ушёл</a>
                        </td>
                        <td>
                            <a class="btn btn-primary js-tracker-action" data-action="done" role="button" href="#">Отработал</a>
                        </td>
                    </tr>
                </table>
                <table class="table mx-auto js-schedule d-none">
                    <thead>
                        <tr>
                            <th>Рабочий день</th>
                            <th>Время</th>
                            <th>Статус</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>