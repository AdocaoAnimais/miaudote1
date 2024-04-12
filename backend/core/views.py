from django.shortcuts import render

# Create your views here.

HOME_PATH = "home.html"

def home(request):
    return render(request, HOME_PATH)