# from django.contrib.auth.models import Group, User
from rest_framework import serializers
from .models import CustomUser

class CustomUserSerializer(serializers.ModelSerializer):
    # nome = serializers.CharField(source='first_name')
    # sobrenome = serializers.CharField(source='last_name')
    class Meta:
        model = CustomUser
        fields = '__all__'