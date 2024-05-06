"""
URL configuration for backend project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from rest_framework import routers
from accounts import views
from rest_framework.authtoken.views import obtain_auth_token


router = routers.DefaultRouter()
router.register(r'users', views.CustomUserViewSet)
router.register(r'pets', views.PetViewSet)


urlpatterns = [
    path('', include('core.urls')), # pagina inicial
    path('admin/', admin.site.urls),
    # path('login/', include('accounts.urls')),
    path('api/', include(router.urls)), #rest framework
    path('api-token-auth/', obtain_auth_token, name='api-token-auth'),  # Token authentication endpoint
]
