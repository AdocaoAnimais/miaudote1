from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib.auth.models import User
from django.contrib import messages
from django.core.mail import EmailMessage, send_mail
from backend import settings
from django.contrib.sites.shortcuts import get_current_site
from django.template.loader import render_to_string
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode
from django.utils.encoding import force_bytes, force_str
from django.contrib.auth import authenticate, login, logout
from . tokens import generate_token
from .forms import CustomUserCreationForm
import os
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail

INDEX_PATH = "index.html"
SIGNIN_PATH = "signin.html"
SIGNUP_PATH = "signup.html"
EMAIL_CONFIRMATION_PATH = "email_confirmation.html"
ACTIVATION_FAILED_PATH = "activation_failed.html"

def mandar_email(request, form):
    first_name = form.Meta.fields[0]
    subject = "Welcome to MiauDote Login!!"
    message = "Hello, Bem-vinde a MiauDote !! \nObrigade por visitar nosso site!\n. We have also sent you a confirmation email, please confirm your email address. \n\nThanking You\nAnubhav Madhav"        
    from_email ="mariana.groff@acad.ufsm.br"
    to_list = ["bigolingroff.mariana@gmail.com"]
    send_mail(subject, message, from_email, to_list, fail_silently=True)
        
    # # Email Address Confirmation Email
    # current_site = get_current_site(request)
    # email_subject = "Confirm your Email @ GFG - Django Login!!"
    # message2 = render_to_string(EMAIL_CONFIRMATION_PATH,{
        
    #     'name': "marizinha",
    #     'domain': current_site.domain,
    #     'uid': urlsafe_base64_encode(force_bytes(myuser.pk)),
    #     'token': generate_token.make_token(myuser)
    # })
    # email = EmailMessage(
    # email_subject,
    # message2,
    # settings.EMAIL_HOST_USER,
    # [myuser.email],
    # )
    # email.fail_silently = True
    # email.send()
    

def create_user(request):

    if request.method == 'POST':
        form = CustomUserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            mandar_email(request, form)


            return redirect('signin')  # Redirect to login page after successful user creation
    else:
        form = CustomUserCreationForm()
    return render(request, 'create_user.html', {'form': form})



# Create your views here.
def home(request):
    return render(request, INDEX_PATH)

# def signup(request):
#     if request.method == "POST":
#         username = request.POST['username']
#         fname = request.POST['fname']
#         lname = request.POST['lname']
#         email = request.POST['email']
#         pass1 = request.POST['pass1']
#         pass2 = request.POST['pass2']
        
#         if User.objects.filter(username=username):
#             messages.error(request, "Username already exist! Please try some other username.")
#             return redirect('signup')
        
#         if User.objects.filter(email=email).exists():
#             messages.error(request, "Email Already Registered!!")
#             return redirect('signup')
        
#         if len(username)>20 or len(username) < 4:
#             messages.error(request, "Username must be over 4 and under 20 charcters!!")
#             return redirect('signup')
        
#         if pass1 != pass2:
#             messages.error(request, "Passwords didn't matched!!")
#             return redirect('signup')
        
#         if not username.isalnum():
#             messages.error(request, "Username must be Alpha-Numeric!!")
#             return redirect('signup')
        
#         myuser = User.objects.create_user(username, email, pass1)
#         myuser.first_name = fname
#         myuser.last_name = lname
#         # myuser.is_active = False
#         myuser.is_active = True
#         myuser.save()
#         messages.success(request, "Your Account has been created succesfully!! Please check your email to confirm your email address in order to activate your account.")
        
#         ###########################################
#         ##### DEALING WITH ACTIVATION EMAILS ####################
#         ###########################################


#         # # Welcome Email
#         # subject = "Welcome to MiauDote Login!!"
#         # message = "Hello " + myuser.first_name + "!! \n" + "Bem-vinde a MiauDote !! \nObrigade por visitar nosso site!\n. We have also sent you a confirmation email, please confirm your email address. \n\nThanking You\nAnubhav Madhav"        
#         # from_email = settings.EMAIL_HOST_USER
#         # to_list = [myuser.email]
#         # send_mail(subject, message, from_email, to_list, fail_silently=True)
        
#         # # Email Address Confirmation Email
#         # current_site = get_current_site(request)
#         # email_subject = "Confirm your Email @ GFG - Django Login!!"
#         # message2 = render_to_string(EMAIL_CONFIRMATION_PATH,{
            
#         #     'name': myuser.first_name,
#         #     'domain': current_site.domain,
#         #     'uid': urlsafe_base64_encode(force_bytes(myuser.pk)),
#         #     'token': generate_token.make_token(myuser)
#         # })
#         # email = EmailMessage(
#         # email_subject,
#         # message2,
#         # settings.EMAIL_HOST_USER,
#         # [myuser.email],
#         # )
#         # email.fail_silently = True
#         # email.send()
        
#         ##############################################

#         return redirect('signin')
        
        
#          return render(request, SIGNUP_PATH)


def activate(request,uidb64,token):
    """
    Função para ativar a conta atraves do gmail
    """
    
    try:
        uid = force_str(urlsafe_base64_decode(uidb64))
        myuser = User.objects.get(pk=uid)
    except (TypeError,ValueError,OverflowError,User.DoesNotExist):
        myuser = None

    if myuser is not None and generate_token.check_token(myuser,token):
        myuser.is_active = True
        # user.profile.signup_confirmation = True
        myuser.save()
        login(request,myuser)
        messages.success(request, "Your Account has been activated!!")
        return redirect('signin')
    else:
        return render(request,'activation_failed.html')


def signin(request):
    if request.method == 'POST':
        username = request.POST['username']
        pass1 = request.POST['pass1']
        
        user = authenticate(request, username=username, password=pass1)
        
        if user is not None:
            login(request, user)
            fname = user.first_name
            messages.success(request, "TOMAR NO SEU CU VC ENTROU!!")
            return render(request, INDEX_PATH,{"fname":fname})
        else:
            messages.error(request, "Bad Credentials!!")
            return redirect('home')
    
    return render(request, SIGNIN_PATH)


def signout(request):
    logout(request)
    messages.success(request, "Logged Out Successfully!!")
    return redirect('home')