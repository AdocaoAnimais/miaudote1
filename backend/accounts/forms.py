from django.contrib.auth.forms import UserCreationForm
from .models import CustomUser
from django import forms
import re


class CustomUserCreationForm(UserCreationForm):

    cpf = forms.CharField(max_length=14, required=True)
    class Meta:
        model = CustomUser
        fields = ['username', 'email', 'bio', 'cpf', ]
    
    # def clean_email(self):
    #     email = self.cleaned_data.get('email')
    #     if email:
    #         if not re.match(r'^[a-zA-Z0-9._]+@[a-zA-Z.]+\.[a-zA-Z]{2,}$', email):
    #             raise ValidationError('Por favor, insira um endereço de e-mail válido.')
    #     return email

    # def clean_cpf(self):
    #     cpf = self.cleaned_data.get('cpf')
    #     if cpf:
    #         # Remove caracteres não numéricos do CPF
    #         cpf = ''.join(filter(str.isdigit, cpf))
    #         # Verifica se o CPF contém apenas números
    #         if not cpf.isdigit():
    #             raise forms.ValidationError('O CPF deve conter apenas números.')
    #         # Formata o CPF adicionando os pontos e o traço
    #         cpf = f'{cpf[:3]}.{cpf[3:6]}.{cpf[6:9]}-{cpf[9:]}'
    #     return cpf