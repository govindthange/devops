# Jenkins Setup

## Step 1. Setup admin credentials (first launch)

1. Open the file ./jenkins/volume-data/secrets/initialAdminPassword
2. Copy the password
3. Launch http://localhost:7777/login
4. You will be sent to `Getting Start` > `Unlock Jenkins screen`
5. Enter the copied key as Administrator password
6. Follow the steps to setup jenkins

> If plugins fail to install, then remove ethernet cable and try connecting to the internet through WiFi network.

### Step 2. Install Plugins

Run [install-plugins.sh](./install-plugins.sh) to install plugins.

```
govind@thinkpad:~/projects/devops$ ./jenkins/install-plugins.sh 
Installing plugin: configuration-as-code:1.40
Done
Installing plugin: email-ext:2.83
Done
Installing plugin: config-file-provider:951.v0461b_87b_721b_
Done
```

Refer [./plugin-installation-guide.md](./plugin-installation-guide.md) for more details.

### Step 3. Restart Jenkins from UI

Hit `[JENKINS URL]/safeRestart` on browser

Click [here](192.9.200.244:7777/safeRestart) to restart jenkins server from GUI.

> If jenkins does not restart in few minutes, then manually stop the container and restart it.

## Step 4. Setup ansible container as SSH server

1. Connect local terminal to ansible container

    Run `docker exec -it devops-ansible /bin/bash`

    ```
    govind@thinkpad:~$ docker exec -it devops-ansible /bin/bash
    root@721d909272b8:/# whoami
    root
    ```

2. Run `ssh-copy-id` command to send the keys to target machine. **Note that you need to perform this once for each user.**

    ```
    root@721d909272b8:/# ssh-copy-id govind@thinkpad
    /usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "/root/.ssh/id_rsa.pub"
    The authenticity of host 'thinkpad (192.168.0.175)' can't be established.
    ED25519 key fingerprint is SHA256:v/PGtNagh7/IkR5na8WOx7ern+NWdWZYeQoo+gGbwxk.
    This key is not known by any other names.
    Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
    /usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed

    /usr/bin/ssh-copy-id: WARNING: All keys were skipped because they already exist on the remote system.
        (if you think this is a mistake, you may want to use -f option)
    ```

3. Run a ping test to ansible controlled target machines
    ```
    root@721d909272b8:/# ansible -i /test/hosts -m ping target1
    target1 | SUCCESS => {
        "ansible_facts": {
            "discovered_interpreter_python": "/usr/bin/python3"
        },
        "changed": false,
        "ping": "pong"
    }

    root@721d909272b8:/# ansible -i /test/hosts -m ping target2
    The authenticity of host '192.9.200.244 (192.9.200.244)' can't be established.
    ED25519 key fingerprint is SHA256:v/PGtNagh7/IkR5na8WOx7ern+NWdWZYeQoo+gGbwxk.
    This host key is known by the following other names/addresses:
        ~/.ssh/known_hosts:1: [hashed name]
    Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
    target2 | SUCCESS => {
        "ansible_facts": {
            "discovered_interpreter_python": "/usr/bin/python3"
        },
        "changed": false,
        "ping": "pong"
    }
    ```

4. Start ssh server in ansible container

    Run `service ssh restart`

    ```
    root@baacda756f60:/# service ssh restart
    Restarting OpenBSD Secure Shell server: sshd.
    ```

## Step 5. Test SSH from jenkins container

1. Initiate ssh session from jenkins

    Run `ssh admin@ansible`

    ```
    root@4831a0a156d7:/# ssh admin@ansible
    The authenticity of host 'ansible (192.168.32.4)' can't be established.
    ECDSA key fingerprint is SHA256:L0A+VfwdD2NDMxuLbrU3D2Vf9eKYCNS0ns7c792Z2lg.
    Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
    Warning: Permanently added 'ansible,192.168.32.4' (ECDSA) to the list of known hosts.
    admin@ansible's password: 
    Linux 721d909272b8 5.15.49-linuxkit-pr #1 SMP Thu May 25 07:17:40 UTC 2023 x86_64

    The programs included with the Debian GNU/Linux system are free software;
    the exact distribution terms for each program are described in the
    individual files in /usr/share/doc/*/copyright.

    Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent
    permitted by applicable law.
    ```

2. Establish fingerprint by running any ansible-playbook

    ```
    admin@721d909272b8:~$ ansible-playbook -i /test/hosts /test/hello-world.yml

    PLAY [Echo] ********************************************************************

    TASK [Gathering Facts] *********************************************************
    The authenticity of host '192.9.200.244 (192.9.200.244)' can't be established.
    ED25519 key fingerprint is SHA256:v/PGtNagh7/IkR5na8WOx7ern+NWdWZYeQoo+gGbwxk.
    This host key is known by the following other names/addresses:
        ~/.ssh/known_hosts:1: [hashed name]
    Are you sure you want to continue connecting (yes/no/[fingerprint])? yes

    ok: [target1]
    ok: [target2]

    TASK [Print debug message] *****************************************************
    ok: [target1] => {
        "msg": "Hello, world!"
    }
    ok: [target2] => {
        "msg": "Hello, world!"
    }

    PLAY RECAP *********************************************************************
    target1                    : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
    target2                    : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
    ```

3. Exit the ssh session

    ```
    admin@721d909272b8:~$ exit
    logout
    Connection to ansible closed.
    ```

4. Initiate SS session w/ sshpass

    We will be testing jenkins job w/ following command.

    ```
    root@4831a0a156d7:/# sshpass -p "changeme" ssh "admin@ansible" ansible-playbook -i /test/hosts /test/hello-world.yml

    PLAY [Echo] ********************************************************************

    TASK [Gathering Facts] *********************************************************
    ok: [target1]
    ok: [target2]

    TASK [Print debug message] *****************************************************
    ok: [target1] => {
        "msg": "Hello, world!"
    }
    ok: [target2] => {
        "msg": "Hello, world!"
    }

    PLAY RECAP *********************************************************************
    target1                    : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
    target2                    : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
    ```


## Step 6. Test jenkins pipeline

> Follow instructions in ./test folder to create tasks that involve running jobs in ansible.

---

# Troubleshooting

## Check connectivity between jenkins and ansible containers

#### Step 1. Start by obtaining the IP address of the ansible container. Use the docker inspect command to get the IP address.

```
govind@thinkpad:~$ docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' devops-ansible 
172.21.0.2
govind@thinkpad:~$ 

```

Take note of the IP address returned by this command.

#### Step 2. Enter jenkins container to test the connectivity.

Use the docker exec command to execute a command inside the container. You want to test connectivity from inside devops-jenkins container to devops-ansible container

```
govind@thinkpad:~$ docker exec -it devops-jenkins /bin/bash
jenkins@e5e16b328575:/$ whoami
jenkins
jenkins@e5e16b328575:/$ 
```

#### Step 3. Use curl to test connectivity to ansible container.

Once inside the jenkins container, you can use standard network troubleshooting tools like ping or curl to test connectivity to the target container.

```
jenkins@e5e16b328575:/$ curl 172.21.0.2
curl: (7) Failed to connect to 172.21.0.2 port 80: Connection refused
jenkins@e5e16b328575:/$ curl ansible
curl: (7) Failed to connect to ansible port 80: Connection refused
jenkins@e5e16b328575:/$ 
```

## SSH from devops-jenkins to devops-ansible container

```
govind@thinkpad:~$ docker exec -it devops-ansible /bin/bash
```

Do SSH (Note that ansible container's SSH port 22 is mapped to host's 8022)

```
ssh ansible@ansible -p 8022
```

---

References:
- [Resetting jenkins password from within container](https://upadhyaymanas3.medium.com/how-to-reset-jenkins-password-in-docker-containers-in-just-10-simple-steps-1370c049bbd7)
