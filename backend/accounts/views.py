from rest_framework import permissions, viewsets
from .models import CustomUser
from .serializers import CustomUserSerializer

class IsSuperuserOrSelf(permissions.BasePermission):
    """
    Restrições de acesso personalizado!!!

    Permite que os superusuários editem qualquer perfil de usuário,
    enquanto os usuários normais só podem editar seu próprio perfil.
    """

    def has_object_permission(self, request, view, obj):
        # Permite superusuarios a tudo
        if request.user.is_superuser:
            return True
        # Permite usuarios modificar somente o proprio perfil
        if request.user.is_authenticated and obj == request.user:
            return True
        # Permitir leitura (GET) para todos os usuários, autenticados ou não
        if request.method in permissions.SAFE_METHODS:
            return True
         # Permitir criação (POST) para todos os usuários, autenticados ou não
        if request.method == 'POST':
            return True
        return False

class CustomUserViewSet(viewsets.ModelViewSet):
    """
    Essa ViewSet automaticamente providencia as ações `list`, `create`, `retrieve`,
    `update` e `destroy`.

    """
    queryset = CustomUser.objects.all().order_by('usuario_id')
    serializer_class = CustomUserSerializer
    permission_classes = [IsSuperuserOrSelf] #comentar fora se nao quiser restrições de acesso



