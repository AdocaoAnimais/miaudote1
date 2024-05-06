from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.core.validators import RegexValidator
from django.db import models


class CustomUser(models.Model):
    """
    Modelo de usuário é criado.
    Todos os campos precisam ser iguais aos do banco de dados.
    """

    contato_regex = RegexValidator(regex=r'^\d{10,11}$', message="Numero de telefone deve conter DDD e 9 ou 8 digitos a mais.")

    # usuario_id = models.IntegerField(primary_key=True, blank=True, default='0', db_column='usuario_id') #usuario_id nao incrementa automaticamente
    usuario_id = models.AutoField(primary_key=True) # usuario_id incrementa automaticamente
    username = models.CharField(max_length=100, unique=True, default='', db_column='username')
    nome = models.CharField(max_length=60, blank=True, db_column='nome')
    sobrenome = models.CharField(max_length=60, blank=True, db_column='sobrenome')
    descricao = models.TextField(max_length=240, blank=True, db_column='descricao')
    cpf = models.CharField(max_length=11, unique=True, null=True, blank=True, db_column='cpf')
    endereco_id = models.IntegerField(null=True, blank=True, db_column='endereco_id')
    contato = models.CharField(validators=[contato_regex],max_length=15, null=True, blank=True, db_column='contato')
    email = models.EmailField(max_length=50, unique=True, db_column='email')
    email_verificado = models.BooleanField(default=False, db_column='email_verificado')
    senha = models.CharField(max_length=50, blank=True, db_column='senha')
    foto = models.ImageField(upload_to='fotos/', null=True, blank=True, db_column='foto')
    perfil_acesso = models.CharField(max_length=1, blank=True, db_column='perfil_acesso')

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['nome', 'sobrenome', 'email']


    @property
    def is_anonymous(self):
        """
        Always return False. This is a way of comparing User objects to
        anonymous users.
        """
        return False

    @property
    def is_authenticated(self):
        """
        Always return False. This is a way of comparing User objects to
        anonymous users.
        """
        return False

    class Meta:
        db_table = 'usuario'

# # Para usar as mesmas informações de nome e sobrenome do Abstract User
# @receiver(pre_save, sender=CustomUser)
# def copy_coisas(sender, instance, **kwargs):
#     instance.nome = instance.first_name
#     instance.sobrenome = instance.last_name
#     instance.usuario_id = instance.id

# def save(self, *args, **kwargs):
#     self.first_name = self.nome
#     self.last_name = self.sobrenome


# ####### É preciso para rodar quando estiver usando AbstractUser #######
# # Define unique related names for groups and user permissions
# groups = models.ManyToManyField(
#     'auth.Group',
#     related_name='custom_user_set',  # Unique related name for groups
#     blank=True,
#     verbose_name='groups',
#     help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.',
# )
# user_permissions = models.ManyToManyField(
#     'auth.Permission',
#     related_name='custom_user_set',  # Unique related name for user permissions
#     blank=True,
#     verbose_name='user permissions',
#     help_text='Specific permissions for this user.',
#     )
