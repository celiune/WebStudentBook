# Additional steps/infos to follow Skyportal setup

## Useful linux commands 

How to access linux files : 
```
explorer.exe .
```
How to open Skyportal files in VS code : 
```
code .
```

## Pre-steps 

Update Node.JS : 
```
sudo npm cache clean -f
sudo npm install -g n
sudo n latest
```

Restart terminal and check version : 
```
node -v
```

If there is a problem with pip version: Update pip and THEN create the virtualenv, otherwise the virtual env will be using the old pip.

If your RAM is 8Go, you will probably need to adjust the WSL RAM (maybe to 4Go). To check the current RAM : 
```
free
```

## Installation-WSL 


2. BEFORE : 

Set up Postgres database on Ubuntu

First, enable SSH : 
https://linuxize.com/post/how-to-enable-ssh-on-ubuntu-20-04/
https://blog.logrocket.com/setting-up-a-remote-postgres-database-server-on-ubuntu-18-04/

To open pg_hba.conf : 
```
sudo nano /etc/postgresql/14/main/pg_hba.conf
```

3. Install geckodriver :

```
wget https://github.com/mozilla/geckodriver/releases/download/v0.32.0/geckodriver-v0.32.0-linux64.tar.gz
tar -xvzf geckodriver*
chmod +x geckodriver
sudo mv geckodriver /usr/local/bin/
```

### Launch 


**On WSL2 :**

 postgresql doesn’t automatically start so every time you activate virtual_env, run this line : 
```
sudo service postgresql start
```

To check if it is already on : 
```
sudo service postgresql status
```

1. First, return to "Source download, Python environment" and if not already done :

```
git clone https://github.com/skyportal/skyportal.git // REPLACE BY YOUR OWN FORK
cd skyportal/
virtualenv skyportal_env
source skyportal_env/bin/activate
```

Make sure you add your new SSH key on your Github account (copy \home\usr\.ssh\id_rsa.pub content into your new SSH id on Github)

Once you are in the virtual env, with postgresql on, you can run `make db_init`.

2. `cp config.yaml.defaults config.yaml`

3. Run `make run` then `make log` 

4. If you see “Registered API schemas” in the logs, and that the web socket has been compiled, that means the app is correctly launched.

5. Open a third terminal with `make run` and `make log` still running, and run `make load_demo_data` in it.

If `make load_demo_data` ends with an “Error log” because some entries couldn’t be add (when you see some posting to … : XXX that means it failed), there can be multiple reasons.

These reasons also apply when in the `make log` (during the make run), you see a message like this one : 
```
Info exited : web socket failed to compile (not expected).
```

- Your WSL doesn’t have enough RAM
- You need to downgrade your Node version (delete the old one before)
- You need to reduce the numbers of python processes running to launch the app
    - in config.yaml, search for `processes : 4` (4 is the default number)
    - replace by 2 or 3 and try to launch again the app, then run make load_demo_data


**On Ubuntu virtual machine :**

First, make sure to have enough storage on your VM. 

2. Configure your database permissions : 

- switch to postgres user :  `su - postgres`
- open the file : `nano /etc/postgresql/14/main/pg_hba.conf`
- add the host lines under the IPv4 section
- `sudo service postgresql restart`

### Launch : 

1. Clone your forked skyportal repository and create the virtual environment (same as WSL)

2. `make db_init`

If you have this kind of error : 
```
psql: error: fe_sendauth: no password supplied
``` 

Set all lines to trust in pg_hba.conf

3. config.yaml doesn't exist under skyportal/, you need to create the file and copy the config.yaml.defaults content
```
cp config.yaml.defaults config.yaml
```

4. `make run` and then `make log`

Wait until a message with .tokens.yaml appears to launch http://localhost:5000 on your browser

If you have an error message like this one : 

```
Permission denied for schema public in 'make log'
Creating tables on database skyportal
Could not connect to database...sleeping 3
(psycopg2.errors.InsufficientPrivilege) permission denied for schema public
```

**Solution**

Execute in the terminal :
```
sudo -u postgres psql postgres
```

Inside of the postgres shell :
```
\c skyportal
GRANT USAGE,CREATE ON SCHEMA public TO postgres; 
GRANT USAGE,CREATE ON SCHEMA public TO skyportal; 
```

