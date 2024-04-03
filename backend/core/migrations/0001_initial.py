# Generated by Django 5.0.3 on 2024-04-02 23:41

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Pet',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='Data de criação')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='Data de atualização')),
                ('active', models.BooleanField(default=True, verbose_name='Ativo?')),
                ('name', models.CharField(max_length=20)),
                ('age', models.PositiveIntegerField(verbose_name='')),
                ('type', models.CharField(choices=[('Cachorro', 'cachorro'), ('Gato', 'gato')], max_length=8)),
                ('image', models.ImageField(upload_to='pets/')),
                ('description', models.TextField()),
            ],
            options={
                'abstract': False,
            },
        ),
    ]
