# from django.contrib.auth.models import Group, User
from rest_framework import serializers
from .models import CustomUser

class CustomUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['id','username', 'first_name', 'last_name', 'email', 'bio', 'cidade', 'cpf', 'telefone']
