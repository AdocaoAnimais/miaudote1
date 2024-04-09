from django.shortcuts import render
from django.http import HttpResponse

# Create your views here.

def home(request):
    return render(reqeust, "accounts/index.html")
def signup(request):
    return render(reqeust, "accounts/signup.html")

def sigin(request):
    return render(reqeust, "accounts/signin.html")

def signout(request):
    pass