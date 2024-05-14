from rest_framework import serializers
from .models import PET


class PetSerializer(serializers.ModelSerializer):
    class Meta:
        model = PET
        fields = '__all__'