from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser

# class CustomUserAdmin(UserAdmin):
#     model = CustomUser
#     list_display = ['username', 'email', 'descricao']  # Add any other fields you want to display

#     # Optionally, define search fields if needed
#     search_fields = ('username', 'email')

    # Optionally, define fieldsets to customize the layout in the admin panel
    # fieldsets = (
    #     (None, {'fields': ('username', 'password')}),
    #     ('Personal Info', {'fields': ('first_name', 'last_name', 'email', 'bio')}),
    #     ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
    #     ('Important Dates', {'fields': ('last_login', 'date_joined')}),
    # )

# Register the CustomUserAdmin class with the CustomUser model
# admin.site.register(CustomUser, CustomUserAdmin)
