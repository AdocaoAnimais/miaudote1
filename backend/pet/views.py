from django.shortcuts import render
from rest_framework import permissions, viewsets
from .serializers import PetSerializer
from . import models

# Create your views here.

class PetViewSet(viewsets.ModelViewSet):
    serializer_class = PetSerializer
    queryset = models.PET.objects.all()