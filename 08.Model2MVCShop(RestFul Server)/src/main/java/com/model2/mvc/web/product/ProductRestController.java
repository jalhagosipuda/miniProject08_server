package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductRestController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping(value="json/addProduct", method=RequestMethod.POST)
	public Product addProduct(@RequestBody Product product) throws Exception{
		
		System.out.println("/addProduct");
		System.out.println(product);
		
		productService.addProduct(product);
		
		return product;
	}

	
	@RequestMapping(value="json/getProduct/{prodNo}")	
	public Product getProduct(@PathVariable int prodNo, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("/getProduct");
		
		return productService.getProduct(prodNo);
	}
	
	
	@RequestMapping(value="json/updateProduct/{prodNo}/{prodName}", method=RequestMethod.GET)
	public Product updateProduct(@PathVariable int prodNo, Model model) throws Exception{
		System.out.println("/updateProductView");
		
		return productService.getProduct(prodNo);
	}
	
	
	@RequestMapping(value="json/updateProduct", method=RequestMethod.POST)
	public Product updateProduct(@RequestBody Product product, Model model , HttpSession session) throws Exception{
		System.out.println("/updateProduct");
		
		productService.updateProduct(product);
		
		return product;
	}
	
	
	@RequestMapping(value="json/listProduct", method=RequestMethod.GET)
	public Map<String, Object> ListProduct() throws Exception{
		
		System.out.println("/listProduct GET");
		
		Search search=new Search();
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		map.put("resultPage",resultPage);
		map.put("search", search);
		
		return map;
	}

	@RequestMapping(value="json/listProduct", method=RequestMethod.POST)
	public Map postListProduct(@RequestBody Search search) throws Exception{
		
		System.out.println("/listProduct POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);

		map.put("resultPage",resultPage);
		map.put("search", search);
		
		return map;
	}
	
}