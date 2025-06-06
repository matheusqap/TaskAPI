document.addEventListener("DOMContentLoaded", () => {
  // Roteamento simples baseado no nome do arquivo na URL
  if (window.location.pathname.endsWith("tasks.html")) {
    const token = localStorage.getItem("token");
    if (!token) {
      window.location.href = "/index.html"; // Redireciona para login se não houver token
      return;
    }
    setupTasksPage();
  } else if (
    window.location.pathname.endsWith("index.html") ||
    window.location.pathname === "/"
  ) {
    setupLoginPage();
  } else if (window.location.pathname.endsWith("register.html")) {
    setupRegisterPage();
  }
});

/**
 * Função para exibir mensagens de feedback para o usuário.
 * @param {string} text - O texto da mensagem.
 * @param {string} type - O tipo de mensagem ('success' ou 'error').
 */
function showMessage(text, type) {
  const messageDiv = document.getElementById("message");
  messageDiv.textContent = text;
  messageDiv.className = `message ${type}`; // Aplica a classe para estilização e visibilidade
}

/**
 * Configura a lógica da página de cadastro.
 */
function setupRegisterPage() {
  const registerForm = document.getElementById("register-form");
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault(); // Impede o envio padrão do formulário
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
      const response = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        showMessage(
          "Usuário cadastrado com sucesso! Redirecionando para login...",
          "success"
        );
        setTimeout(() => {
          window.location.href = "/index.html"; // Redireciona para a página de login
        }, 2000);
      } else {
        const errorText = await response.text();
        throw new Error(errorText || "Erro desconhecido");
      }
    } catch (error) {
      showMessage(`Erro ao cadastrar: ${error.message}`, "error");
    }
  });
}

/**
 * Configura a lógica da página de login.
 */
function setupLoginPage() {
  const loginForm = document.getElementById("login-form");
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
      const response = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const token = await response.text();
        localStorage.setItem("token", token); // Salva o token no armazenamento local
        showMessage("Login bem-sucedido! Redirecionando...", "success");
        setTimeout(() => {
          window.location.href = "/tasks.html"; // Redireciona para a página de tarefas
        }, 1000);
      } else {
        const errorText = await response.text();
        throw new Error(errorText || "Credenciais inválidas");
      }
    } catch (error) {
      showMessage(`Erro no login: ${error.message}`, "error");
    }
  });
}

/**
 * Configura a lógica da página de tarefas.
 */
function setupTasksPage() {
  const logoutButton = document.getElementById("logout-button");
  const taskForm = document.getElementById("task-form");

  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("token"); // Remove o token ao sair
    window.location.href = "/index.html";
  });

  taskForm.addEventListener("submit", createTask);

  loadTasks(); // Carrega as tarefas assim que a página é configurada
}

/**
 * Carrega e exibe as tarefas do usuário autenticado.
 */
async function loadTasks() {
  const token = localStorage.getItem("token");
  const taskListDiv = document.getElementById("task-list");
  taskListDiv.innerHTML = ""; // Limpa a lista antes de carregar novamente

  try {
    const response = await fetch("/tasks/list", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`, // Envia o token para autenticação
      },
    });

    if (!response.ok) {
      if (response.status === 403) {
        // Forbidden - token inválido/expirado
        localStorage.removeItem("token");
        window.location.href = "/index.html";
      }
      throw new Error("Não foi possível carregar as tarefas.");
    }

    const tasks = await response.json();
    if (tasks.length === 0) {
      taskListDiv.innerHTML =
        "<p>Nenhuma tarefa encontrada. Adicione uma nova!</p>";
    } else {
      tasks.forEach((task) => {
        const taskElement = document.createElement("div");
        taskElement.className = "task-item";
        taskElement.innerHTML = `<h3>${task.title}</h3><p>${
          task.description || "Sem descrição"
        }</p>`;
        taskListDiv.appendChild(taskElement);
      });
    }
  } catch (error) {
    showMessage(error.message, "error");
  }
}

/**
 * Envia uma nova tarefa para a API e atualiza a lista.
 * @param {Event} e - O evento de submit do formulário.
 */
async function createTask(e) {
  e.preventDefault();
  const token = localStorage.getItem("token");
  const title = document.getElementById("title").value;
  const description = document.getElementById("description").value;

  try {
    const response = await fetch("/tasks/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ title, description, status: "PENDING" }),
    });

    if (!response.ok) {
      throw new Error("Erro ao criar tarefa");
    }

    document.getElementById("task-form").reset(); // Limpa o formulário
    loadTasks(); // Recarrega a lista de tarefas para incluir a nova
  } catch (error) {
    showMessage(error.message, "error");
  }
}
