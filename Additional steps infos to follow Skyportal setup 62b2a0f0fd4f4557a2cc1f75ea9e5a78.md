# Additional steps/infos to follow Skyportal setup

## Useful linux commands:

How to access linux files : `explorer.exe` .

How to open Skyportal files in VS code : `code` .

## Pre-steps:

- [ ]  Update Node.JS :

```bash
sudo npm cache clean -f
sudo npm install -g n
sudo n latest
```

- [ ]  Restart terminal and check version

```bash
node -v
```

- [ ]  If there is a problem with pip version: update pip and THEN create the virtualenv, otherwise the virtual env will be using the old pip.
- [ ]  If your RAM is 8Go, you will probably need to adjust the WSL RAM (maybe to 4Go). To check the current RAM

```bash
free
```

## Installation-WSL

1. BEFORE :`Set up Postgres database on Ubuntu`

- [ ]  First, enable SSH :

> [https://linuxize.com/post/how-to-enable-ssh-on-ubuntu-20-04/](https://linuxize.com/post/how-to-enable-ssh-on-ubuntu-20-04/)
> 

> [https://blog.logrocket.com/setting-up-a-remote-postgres-database-server-on-ubuntu-18-04/](https://blog.logrocket.com/setting-up-a-remote-postgres-database-server-on-ubuntu-18-04/)
> 

To `open pg_hba.conf` 

```bash
sudo nano /etc/postgresql/14/main/pg_hba.conf
```

2. Install geckodriver :

```bash
wget https://github.com/mozilla/geckodriver/releases/download/v0.32.0/geckodriver-v0.32.0-linux64.tar.gz
tar -xvzf geckodriver*
chmod +x geckodriver
sudo mv geckodriver /usr/local/bin/
```

### **Launch :**

On WSL2, `postgresql` doesn’t automatically start so every time you activate `virtual_env`, run this line:

```bash
sudo service postgresql start
```

To check if it is already on :

```bash
sudo service postgresql status
```

1. First, return to `Source download, Python environment` and if not already done :

```bash
git clone https://github.com/skyportal/skyportal.git // REPLACE BY YOUR OWN FORK
cd skyportal/
virtualenv skyportal_env
source skyportal_env/bin/activate
```

Make sure you add your new SSH key on your Github account (copy `\home\usr\.ssh\id_rsa.pub` content into your new SSH id on Github)

Once you are in the virtual environment, with `postgresql` on, you can run: 

```bash
make db_init
cp config.yaml.defaults config.yaml
make run
```

On another terminal, also in the virtual environment:

```bash
make log
```

If you see `Registered API schemas` in the logs, and that the web socket has been compiled, that means the app is correctly launched 😆

On a third terminal, without closing the other two, still in virtual environment, run:

```bash
make load_demo_data
```

If `make load_demo_data` ends with an `Error log` because some entries couldn’t be add (when you see some posting to … : XXX that means it failed), there can be multiple reasons.

These reasons also apply when in the `make log` (during the `make run`), you see a message like this one : `Info exited : web socket failed to compile (not expected)`.

- Your WSL doesn’t have enough RAM
- You need to downgrade your Node version (delete the old one before)
- You need to reduce the numbers of python processes running to launch the app
    - in `config.yaml`, search for “processes : 4” (4 is the default number)
    - replace by 2 or 3 and try to launch again the app, then run `make load_demo_data`

## **On Ubuntu virtual machine:**

First, make sure to have enough storage on your VM.

Configure your database permissions :

- [ ]  Switch to postgres user: `su - postgres`
- [ ]  Open the file : `nano /etc/postgresql/14/main/pg_hba.conf`
    
    *You can replace `14` by your postgresql version*
    
- [ ]  Add the host lines under the IPv4 section
- [ ]  `sudo service postgresql restart`

Before launching the app, check `node.js` version, if it’s not the good one: check [Pre-steps:](https://www.notion.so/Pre-steps-b7f64f3f84c046f8bd59f295442b42fd) 

### Launch:

1. Clone your forked skyportal repository and create the virtual environment (same as WSL)
2. In the terminal, run: 

```bash
make run db_init
```

If you get: `psql: error: fe_sendauth: no password supplied`

⇒ Set all lines to trust in pg_hba.conf

1. `config.yaml` file doesn't exist under `skyportal/`, so create the file and copy the `config.yaml.defaults` content

```bash
cp config.yaml.defaults config.yaml
make run
```

1. On another terminal, in the same virtual environment:

```bash
make log
```

Wait until a message with `.tokens.yaml` appears to launch http://localhost:5000 on your browser

If you have an error message like this in `make log`:

```bash
Creating tables on database skyportal
Could not connect to database...sleeping 3
(psycopg2.errors.InsufficientPrivilege) permission denied for schema public
```

⇒ Solution *:*

In the terminal :

```bash
sudo -u postgres psql postgres
```

![Untitled](Additional%20steps%20infos%20to%20follow%20Skyportal%20setup%2062b2a0f0fd4f4557a2cc1f75ea9e5a78/Untitled.png)

In the postgres shell :

```bash
*\c skyportal
GRANT USAGE,CREATE ON SCHEMA public TO postgres;
GRANT USAGE,CREATE ON SCHEMA public TO skyportal;*
```

Now you can start working on skyportal, hope it helped you ! 🚀