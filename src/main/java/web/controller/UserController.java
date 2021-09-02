package web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.dao.JpaRoleDao;
import web.dao.JpaUserDao;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
	private final JpaUserDao userDao;
	private final JpaRoleDao roleDao;

	public UserController(JpaUserDao userDao, JpaRoleDao roleDao) {
		this.userDao = userDao;
		this.roleDao = roleDao;
	}

	@GetMapping("/")
	public String main() {
		return "index";
	}

	@GetMapping(value = "/admin")
	public String index(Model model) {
		model.addAttribute("list", userDao.index());
		return "admin";
	}
	@GetMapping("/{count}")
	public String show(@PathVariable("count") int count, Model model) {
		model.addAttribute("user", userDao.show(count));
		return "show";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", roleDao.getAllRoles());
		return "new";
	}
	@PostMapping()
	public String create(@ModelAttribute("user") User user,
						 @RequestParam(value = "checkbox_roles", required = false) String[] checkboxRoles)
	{
		Set<Role> roles = new HashSet<>();
		for (String role : checkboxRoles) {
			roles.add(roleDao.getRoleByName(role));
		}
		user.setRoles(roles);
		userDao.save(user);
		return "redirect:/admin";
	}

	@GetMapping("/{count}/edit")
	public String edit(@PathVariable("count") int count, Model model) {
		model.addAttribute("user", userDao.show(count));
		model.addAttribute("roles", roleDao.getAllRoles());
		return "/edit";
	}
	@PostMapping("/{count}")
	public String update(@ModelAttribute("user") User user,
						 @PathVariable("count") int count,
						 @RequestParam(value = "checkbox_roles", required = false) String[] checkboxRoles) {
		Set<Role> roles = new HashSet<>();
		for (String role : checkboxRoles) {
			roles.add(roleDao.getRoleByName(role));
		}

		user.setRoles(roles);
		userDao.update(user);
		return "redirect:/admin";
	}
	@PostMapping("/{count}/delete")
	public String delete(@PathVariable("count") int count) {
		userDao.delete(count);
		return "redirect:/admin";
	}

	@GetMapping("/user")
	public String userPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("currentUserRoles", user.getRoles()
				.toString()
				.replace("[", "")
				.replace("ROLE_","")
				.replace("]",""));
		return "user";
	}


	@GetMapping("hello")
	public String printWelcome(ModelMap model) {
		List<String> messages = new ArrayList<>();
		messages.add("Hello!");
		messages.add("I'm Spring MVC-SECURITY application");
		messages.add("5.2.0 version by sep'19 ");
		model.addAttribute("messages", messages);
		return "hello";
	}

	@GetMapping("login")
	public String loginPage() {
		return "login";
	}
}