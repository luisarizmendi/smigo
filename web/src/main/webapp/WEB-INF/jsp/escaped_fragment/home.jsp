<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../header.jsp"/>
<jsp:include page="nav.jsp"/>

<div ng-view="" class="angular-view ng-scope">
    <div class="jumbotron ng-scope" style="text-align: center;">
        <div class="container">

            <h1 class="ng-binding">Garden planning</h1>
            <p class="ng-binding">
                Smigo is a free and simple vegetable garden planner where you can design a layout of your garden online, assist with companion planting and crop rotation. Free, open source and easy to use. Support for all platforms including iOS, iPhone, iPad, Android, Windows and Mac.
            </p>
            <p><a class="btn btn-primary btn-lg ng-binding" href="/garden" role="button">Try it now</a></p>
        </div>
    </div>
    <div class="container ng-scope">
        <div class="row">
            <div class="col-md-4">
                <h2 class="ng-binding">Online</h2>
                <p class="ng-binding">No need to download or install software. Smigo is web based app that works on all platforms. The only thing you need is a computer, a phone or a tablet with internet connection.</p>
            </div>
            <div class="col-md-4">
                <h2 class="ng-binding">The planner</h2>
                <p class="ng-binding">Design a layout plan of your kitchen garden. Choose from hundredes of vegetables in our database or add your own with one easy click. As you add more beds, the grid expands. Smigo works great with square foot gardening.</p>
            </div>
            <div class="col-md-4">
                <img ng-src="/static/icon/snapshot.png" alt="vegetable garden layout" class="img-rounded full-width" src="/static/icon/snapshot.png">
            </div>
        </div>

        <div class="row">
            <div class="col-md-4 hidden-xs hidden-sm">
                <img ng-src="/static/icon/kitchengarden.jpg" alt="kitchen garden" class="img-rounded full-width" src="/static/icon/kitchengarden.jpg">
            </div>
            <div class="col-md-8">
                <h2 class="ng-binding">Free</h2>
                <p class="ng-binding">Free and Open Source so you can view the source code to this app, add your own changes and participate in the developement. Code available at
                    <a href="https://github.com/nosslin579/smigo">Github</a></p>
                <p class="ng-binding">Available in other languages like Spanish, German and Swedish. Partially translated to French, Romanian and Czech.</p>
            </div>
        </div>
        <!-- ngInclude: 'footer.html' -->
        <div ng-include="'footer.html'" class="ng-scope">
            <hr style="border-top-color:#B8B8B8;margin-top: 100px" class="ng-scope">

            <div class="row ng-scope">
                <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8">
                    <footer>
                        <div class="row">
                            <div class="col-sm-offset-1 col-xs-4">
                                <div style="text-decoration: underline;" class="ng-binding">About</div>
                                <a href="https://github.com/nosslin579/smigo" class="ng-binding">Source code</a><br>
                                <a href="/static/terms-of-service.html" rel="nofollow" class="ng-binding">Terms of Service</a><br>
                                <a href="/help" class="ng-binding">Help</a><br>
                                <a href="/forum" class="ng-binding">Forum</a><br>
                            </div>
                            <div class="col-xs-4">
                                <div style="text-decoration: underline;" class="ng-binding">Contact</div>
                                <a href="http://www.reddit.com/r/smigo" target="_blank">Reddit</a><br>
                                <a href="https://www.facebook.com/smigogarden" target="_blank">Facebook</a><br>
                                <a href="https://www.twitter.com/smigogarden" target="_blank">Twitter</a><br>
                                <a href="http://se.linkedin.com/pub/christian-nilsson/3b/798/a5b/" target="_blank">Linkedin</a><br>
                            </div>
                            <div class="col-xs-4 col-xs-3">
                                <div style="text-decoration: underline;" class="ng-binding">Links</div>
                                <a href="http://en.wikipedia.org/wiki/Companion_planting" target="_blank" class="ng-binding">Companion planting</a><br>
                                <a href="http://en.wikipedia.org/wiki/Square_foot_gardening" target="_blank" class="ng-binding">Square foot gardening</a><br>
                                <a href="http://sourceforge.net/projects/kitchengarden" target="_blank" class="ng-binding">Kitchen garden aid</a><br>
                                <a href="http://en.wikipedia.org/wiki/Crop_rotation" target="_blank" class="ng-binding">Crop rotation</a><br>
                            </div>
                        </div>
                        <div class="row" style="margin: 16px;">
                            <div class="text-center">Copyright (C) 2011-2015 Christian Nilsson - christian1195@gmail.com</div>
                        </div>
                    </footer>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>