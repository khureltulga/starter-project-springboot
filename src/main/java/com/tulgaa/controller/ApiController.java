package com.tulgaa.controller;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.tulgaa.model.Post;
import com.tulgaa.model.MyUser;
import com.tulgaa.model.UserDao;
import com.tulgaa.repository.UserRepository;

@RestController
public class ApiController {
	
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDao _userDao;
	
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = "/data/users", method = RequestMethod.GET)
	public DataTablesOutput<MyUser> getUsers(@Valid DataTablesInput input) {
		return userRepository.findAll(input);
	}

	@RequestMapping(value = "/data/users", method = RequestMethod.POST)
	public String createUser(@RequestBody String jsonstr) throws ClassNotFoundException, JSONException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		MyUser u = null;
		JSONObject result = new JSONObject();
		JSONObject obj=new JSONObject(jsonstr);
		String action = obj.getString("action");
		JSONObject data = obj.getJSONObject("data");

		Class classTemp = Class.forName("com.tulgaa.model."+obj.getString("model"));
		//System.out.println(classTemp.getDeclaredFields());
		//Object objTemp =classTemp.newInstance();
		Field[] fields = classTemp.getDeclaredFields();
		switch (action) {
		case "create":
			JSONObject d = data.getJSONObject("0");
			//u = new User(d.getString("email"),d.getString("name"));
			u = (MyUser) classTemp.newInstance();
			for(int i=0;i<data.getNames(data).length;i++){
				d = data.getJSONObject(JSONObject.getNames(data)[i]);
				for( Field field : fields ){
					if (d.has(field.getName())){
						u.setField(field.getName(), field.getType().cast(d.get(field.getName())));
					}
				}
			}
			userRepository.save(u);
			result.put("data", u);
			break;
		case "edit":
			for(int i=0;i<data.getNames(data).length;i++){
				u = (MyUser)_userDao.getByIdGlobal(Long.parseLong(data.getNames(data)[i]),obj.getString("model"));
				if (!u.equals(null)){
					d = data.getJSONObject(JSONObject.getNames(data)[i]);

					for( Field field : fields ){
						if (d.has(field.getName())){
							u.setField(field.getName(), field.getType().cast(d.get(field.getName())));
						}
					}

					userRepository.save(u);
					result.put("data", u);
				}
			}
			break;
		case "remove":
			for(int i=0;i<data.getNames(data).length;i++){
				u = (MyUser)_userDao.getByIdGlobal(Long.parseLong(data.getNames(data)[i]),obj.getString("model"));
				if (!u.equals(null)){
					userRepository.delete(u);
					//result.put("data", "");
					//_userDao.updateGlobal(u);
				}
			}
			break;
		default:
			break;
		}
		/*try {
			String decoded = URLDecoder.decode(jsonstr, "UTF-8");
			System.out.println(decoded);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*User u = new User(name, email);
		_userDao.saveGlobal(u);
		return u;*/
		return result.toString();
	}

	/*	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = "/data/users", method = RequestMethod.PUT)
	public User updateUser(@RequestParam Map<String,String> allRequestParams, @RequestParam("id") int id) {
		//allRequestParams.values();
		Iterator entries = allRequestParams.entrySet().iterator();
		User u = _userDao.getById(id);
		if (!u.equals(null)){
			while (entries.hasNext()) {
				Map.Entry<String, String> thisEntry = (Map.Entry<String, String>) entries.next();
				if (thisEntry.getKey().equals("data." + id + ".email")){
					u.setEmail(thisEntry.getValue());
				}
				if (thisEntry.getKey().equals("data." + id + ".name")){
					u.setName(thisEntry.getValue());
				}
				//System.out.println(thisEntry.getKey() + " ====  " + thisEntry.getValue());
			}
			_userDao.updateGlobal(u);
		}
		return u;
	}*/


	@RequestMapping(value="/api/get-by-email")
	@ResponseBody
	public MyUser getByEmail(String email) {
		MyUser user;
		String userId;
		try {
			user = _userDao.getByEmail(email);
			userId = String.valueOf(user.getId());
		}
		catch(Exception ex) {
			return null;
		}
		return user;
	}

	@RequestMapping(value="/api/get-users", method=RequestMethod.POST)
	@ResponseBody
	public List<MyUser> getUsersApi() {
		List<MyUser> user;
		String userId;
		user = _userDao.getAll();
		return user;
	}

	@RequestMapping(value="/api/save", method=RequestMethod.POST)
	@ResponseBody
	public MyUser create(@RequestBody String jsonstr) {
		MyUser user = null;
		JSONObject obj=new JSONObject(jsonstr);
		try {
			BCryptPasswordEncoder bp = new BCryptPasswordEncoder();
			/*MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(obj.getString("password").getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			System.out.println("Hex format : " + sb.toString());*/
			user = new MyUser(obj.getString("email"), obj.getString("name"), bp.encode(obj.getString("password")));
			_userDao.save(user);
			/*user = new User(email, name);
			_userDao.save(user);*/
		}
		catch(Exception ex) {
			return user;
		}
		return user;
	}

	@RequestMapping(value="/api/get-user-detail", method=RequestMethod.POST)
	@ResponseBody
	public MyUser getUserDetail(@RequestBody String jsonstr, HttpServletRequest request, HttpServletResponse response) {
		MyUser user = null;
		JSONObject obj=new JSONObject(jsonstr);
		user = _userDao.getByUserID(Long.parseLong(obj.getString("id")));
		return user;
	}


	@RequestMapping(value="/api/update-post", method=RequestMethod.POST)
	@ResponseBody
	public Object updatePost(@RequestBody String jsonstr, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj=new JSONObject(jsonstr);
		Post post = null;
		ArrayList result = (ArrayList) _userDao.getGlobal("Post", "id = " + obj.getInt("id"));
		if (!result.isEmpty()){
			post = (Post) result.get(0);
		}

		if (post != null){
			post.setTitle(obj.getString("title"));
			post.setContent(obj.getString("content"));
			_userDao.updateGlobal(post);
		}

		return post;
	}

	@RequestMapping(value="/api/create-post", method=RequestMethod.POST)
	@ResponseBody
	public Object createPost(@RequestBody String jsonstr, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj=new JSONObject(jsonstr);
		Post post = new Post();
		post.setTitle(obj.getString("title"));
		post.setContent(obj.getString("content"));
		post.setUserId(obj.getInt("user_id"));
		_userDao.saveGlobal(post);

		return post;
	}

	@RequestMapping(value="/api/delete-post", method=RequestMethod.POST)
	@ResponseBody
	public void deletePost(@RequestBody String jsonstr, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj=new JSONObject(jsonstr);
		Post post = null;
		ArrayList result = (ArrayList) _userDao.getGlobal("Post", "id = " + obj.getInt("id"));
		if (!result.isEmpty()){
			post = (Post) result.get(0);
		}

		if (post != null){
			_userDao.deleteGlobal(post);
		}
	}

	@RequestMapping(value="/api/get-all-post", method=RequestMethod.GET)
	@ResponseBody
	public Object getAllPosts(HttpServletRequest request, HttpServletResponse response) {
		/*User user = null;
		JSONObject obj=new JSONObject(jsonstr);
		user = _userDao.getByUserID(Long.parseLong(obj.getString("id")));*/
		return _userDao.getGlobal("Post", "");
	}

	@RequestMapping(value="/api/login", method=RequestMethod.POST)
	@ResponseBody
	public MyUser loginCheck(@RequestBody String jsonstr, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {

		JSONObject obj=new JSONObject(jsonstr);
		MyUser user = _userDao.getByEmail(obj.getString("email"));
		if (user != null){
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(obj.getString("password").getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			if (user.getPassword().equals(sb.toString())){
				Cookie cookie = new Cookie("user_id", user.getId() + "");
				cookie.setMaxAge(3 * 24 * 60 * 60);
				cookie.setPath("/");
				response.addCookie(cookie); 
			}
			else{
				Cookie cookie = new Cookie("user_id", "0");
				cookie.setMaxAge(3 * 24 * 60 * 60);
				cookie.setPath("/");
				response.addCookie(cookie); 
			}
		}
		return user;
	}
}
