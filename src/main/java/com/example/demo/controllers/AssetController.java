package com.example.demo.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.restservice.Asset;
import com.example.demo.service.AssetService;

@RestController
@RequestMapping(path="/demo")
public class AssetController {
	
	@Autowired
	AssetService assetService;

	private static  Logger logger = LogManager.getLogger(AssetController.class);


	/**
	 * Funciton for creating a new asset with it's mandatory attributes.
	 * @param name
	 * @param descripcion
	 * @param tipo
	 * @param serial
	 * @param numeroInternoInventario
	 * @param peso
	 * @param alto
	 * @param ancho
	 * @param largo
	 * @param valor
	 * @param fechaDeCompra
	 * @return
	 */
	@PostMapping("/new")
	public ResponseEntity<Object> greeting(@RequestParam(value = "name") String name,@RequestParam(value = "descripcion") String descripcion,@RequestParam(value = "tipo") String tipo,
			@RequestParam(value = "serial") String serial,@RequestParam(value = "numeroInternoInventario") String numeroInternoInventario,@RequestParam(value = "peso") Double peso,
			@RequestParam(value = "alto") Double alto,@RequestParam(value = "ancho") Double ancho,@RequestParam(value = "largo") Double largo,
			@RequestParam(value = "valor") Double valor,@RequestParam(value = "fechaDeCompra") String fechaDeCompra) {
		logger.info ("greetings ");
		logger.info (name);
		logger.info (descripcion);
		logger.info (tipo);
		logger.info (serial);
		logger.info (numeroInternoInventario);
		logger.info (peso);
		logger.info (alto);
		logger.info ("Ancho: ");
		logger.info (ancho);
		logger.info (valor);
		logger.info (fechaDeCompra);
		
		Optional<Asset> responseAsset = assetService.createAsset(name, descripcion, tipo, serial, numeroInternoInventario,
			peso, alto, ancho, largo, valor, fechaDeCompra);
		if(responseAsset.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(responseAsset.get());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseAsset.get());
		}
	}
	
	/**
	 * Function for updating the values of an asset.
	 * @param assetId
	 * @param name
	 * @param descripcion
	 * @param tipo
	 * @param serial
	 * @param numeroInternoInventario
	 * @param peso
	 * @param alto
	 * @param ancho
	 * @param largo
	 * @param valor
	 * @param fechaDeCompra
	 * @return
	 */
	@PostMapping("/update")
	public ResponseEntity<Object> updateAsset(@RequestParam(value = "assetId") Integer assetId, @RequestParam(value = "name") String name,@RequestParam(value = "descripcion") String descripcion,@RequestParam(value = "tipo") String tipo,
			@RequestParam(value = "serial") String serial,@RequestParam(value = "numeroInternoInventario") int numeroInternoInventario,@RequestParam(value = "peso") Double peso,
			@RequestParam(value = "alto") Double alto,@RequestParam(value = "ancho") Double ancho,@RequestParam(value = "largo") Double largo,
			@RequestParam(value = "valor") Double valor,@RequestParam(value = "fechaDeCompra") String fechaDeCompra) {
		try {
			Optional<Asset> response = assetService.updateAsset(assetId, name,descripcion, tipo, serial, numeroInternoInventario, peso, alto, ancho, 
				largo, valor, fechaDeCompra);
			return	ResponseEntity.status(HttpStatus.OK).body(response.get());
		} catch(Exception e) {
			logger.debug(e.getMessage());
			if(e.getMessage()== "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El activo con id:" + assetId  +" no se encontró");
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el activo, tiene parametros nulos");
			}
			
		}catch(Error e) {
			logger.debug(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al actualizar el activo");
		}
		 
				
	}

	/**
	 * Function for deleting an asset receiving as parameter an asset's id
	 * @param assetId
	 * @return
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<Object> delete(@RequestParam Integer assetId){
		
		try {
			assetService.deleteById(assetId);
			return ResponseEntity.status(HttpStatus.OK).body("Se eliminó el activo: " + assetId);
						
		}catch (Exception e) {
			
			if(e.getMessage() == "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El activo a borrar no se encontró");
			}
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al borrar el activo");
		}		
	}

	/**
	 * Function to list a set of assets
	 * @return the list of all assets with an http ok status
	 */
	@GetMapping("/all")
	public ResponseEntity<Object> getAllAssets() {		
		return  ResponseEntity.status(HttpStatus.OK).body(assetService.getAllAssets());
	}

	/**
	 * Function that gets an specific asset
	 * @param assetId
	 * @return the asset with the correct status
	 */
	@GetMapping("/get")
	public  ResponseEntity<Object> getById(@RequestParam Integer assetId) {
		try {
			Asset asset = assetService.getById(assetId);
			return ResponseEntity.status(HttpStatus.OK).body(asset);
		}catch(Exception e) {
			if(e.getMessage()== "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El activo con id:" + assetId  +" no se encontró");
			}
			else {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al consultar el activo");
			}
		}
	}
	
	@GetMapping("/findByTipoDeCompra")
	public  ResponseEntity<Object> getByTipo(@RequestParam String tipoCompra) {
		try {
			Iterable<Asset> assets = assetService.findByTipoCompra(tipoCompra);
			return ResponseEntity.status(HttpStatus.OK).body(assets);
		}catch(Exception e) {
			if(e.getMessage()== "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen activos de tipo:" + tipoCompra );
			}
			else {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al consultar el activo con tipo de compra: " + tipoCompra );
			}
		}
	}
	
	@GetMapping("/findByFechaCompra")
	public  ResponseEntity<Object> getByFecha(@RequestParam String fechaCompra) {
		try {
			Iterable<Asset> assets = assetService.findByFechaCompra(fechaCompra);
			return ResponseEntity.status(HttpStatus.OK).body(assets);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			if(e.getMessage()== "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen activos con fecha:" + fechaCompra );
			}
			else {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al consultar el activo con fecha" + fechaCompra);
			}
		}
	}
	
	@GetMapping("/findBySerial")
	public  ResponseEntity<Object> getBySerial(@RequestParam String serial) {
		try {
			Iterable<Asset> assets = assetService.findBySerial(serial);//Id(assetId).get();
			return ResponseEntity.status(HttpStatus.OK).body(assets);
		}catch(Exception e) {
			if(e.getMessage()== "No value present") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen activos de serial:" + serial );
			}
			else {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al consultar el activo");
			}
		}
	}
}
