## Как сделать вклад в проект
Если вы решили сделать свой вклад в проект. Ниже описан порядок действий.

Создайте форк проекта.

Клонируйте себе ваш форк.
```bash
git clone https://github.com/<your_username>/hexlet-correction.git
```

Добавте `remote` на оригинальный репозиторий под именем `upstream`.
```bash
git remote add upstream https://github.com/Hexlet/hexlet-correction.git
```

Создайте новую ветку под фичу.
```bash
git checkout -b new-feature
```

Добавьте необходимие изменения в проект. Зафиксируйте их коммитом.
```bash
git commit
```

Переключитесь на ветку `master`, обновите её, вернитесь на ветку с фичей.
```bash
git checkout master
git pull upstream --rebase
git checkout new-feature
```

Добавте изменения из ветки `master` в ветку с фичей.
```bash
git rebase master
```

Если возникли конфликты, решите их и возобновите обновление ветки.
```bash
git commit
git rebase --continue
```

Отправте ветку с фичей в ваш форк. А затем создайте Pull Request.
```bash
git push origin new-feature
```

Ожидайте ревью вашего PR.
