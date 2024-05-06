# from django.contrib.auth.models import Group, User
from rest_framework import serializers
from .models import CustomUser
from core.models import PET

class CustomUserSerializer(serializers.ModelSerializer):
    """
    Transforma o modelo CustomUser em JSON
    """
    # # maneira de renomear o campo padrao do Abstract User (nao necessario no momento)
    # nome = serializers.CharField(source='first_name')
    # sobrenome = serializers.CharField(source='last_name')
    class Meta:
        model = CustomUser
        fields = '__all__'

class PetSerializer(serializers.ModelSerializer):
    class Meta:
        model = PET
        fields = '__all__'