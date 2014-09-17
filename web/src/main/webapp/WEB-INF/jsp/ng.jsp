<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<jsp:include page="header.jsp"/>
<body ng-app="smigoModule">

<%@ include file="menu.html" %>

<div ng-view></div>

<script type="application/javascript">

    <%@ include file="Customization.js" %>
    <%@ include file="app.js" %>
    (function () {
        <%@ include file="garden/SpeciesFilter.js" %>
        <%@ include file="garden/GardenController.js" %>
        <%@ include file="garden/GardenService.js" %>
        <%@ include file="garden/SpeciesService.js" %>
        <%@ include file="MainMenuController.js" %>
        <%@ include file="user/AcceptTermsOfServiceController.js" %>
        <%@ include file="user/RegisterController.js" %>
        <%@ include file="user/LoginController.js" %>
        <%@ include file="user/UserService.js" %>
        <%@ include file="garden/PlantService.js" %>
        <%@ include file="garden/RememberScrollDirective.js" %>
        <%@ include file="EqualsDirective.js" %>
        <%@ include file="TranslateFilter.js" %>
        <%@ include file="Http.js" %>
    })();
</script>

<%--########## ng views ##############--%>
<script type="text/ng-template" id="garden.html">
    <%@ include file="ng-garden.html" %>
</script>
<script type="text/ng-template" id="help.html">
    <%@ include file="ng-help.html" %>
</script>
<script type="text/ng-template" id="login.html">
    <%@ include file="ng-login.html" %>
</script>

<%@ include file="accept-terms-of-service-modal.html" %>

</body>

<jsp:include page="footer.jsp"/>