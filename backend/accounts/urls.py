from django.contrib import admin
from django.urls import path, include
from . import views

urlpatterns = [
   path('', views.home, name="home"),
   path('signup', views.create_user, name = "signup"),
   path('signin', views.signin, name = "signin"),
   path('signout', views.signout, name = "signout"),
   path('activate/<uidb64>/<token>', views.activate, name = "activate"),
   #path('create-user', views.create_user, name='create_user'),
]
