package com.generation.projetopanc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.generation.projetopanc.databinding.FragmentNovoProdutoBinding
import com.generation.projetopanc.model.Categoria
import kotlinx.coroutines.selects.select
import java.util.*

class NovoProdutoFragment : Fragment() {

    private lateinit var binding: FragmentNovoProdutoBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var categoriaSelecionada = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNovoProdutoBinding.inflate(layoutInflater, container, false)

        mainViewModel.listCategoria()

        mainViewModel.myCategoriaResponse.observe(viewLifecycleOwner){
            response -> Log.d("Requisicao", response.body().toString())
            spinnerCategoria(response.body())
        }

        binding.buttonCadastrar.setOnClickListener {
            inserirNoBanco()
        }
        return binding.root
    }

    fun spinnerCategoria(listCategoria: List<Categoria>?){
        if (listCategoria != null) {
            binding.spinnerCategoria.adapter =
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    listCategoria
                )
            binding.spinnerCategoria.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val selected = binding.spinnerCategoria.selectedItem as Categoria
                        categoriaSelecionada = selected.id
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        }
    }


    private fun validarCampos(
        nome: String,
        imagem: String,
        valor: String,
        descricao: String,
        quantidade: String
    ): Boolean {

        return !(
                (nome == "" || nome.length < 3 || nome.length > 40) ||
                        (imagem == "") ||
                        (valor == "") ||
                        (descricao == "" || nome.length < 5 || nome.length > 240) ||
                        (quantidade == "")
                )
    }

    private fun inserirNoBanco() {
        val nome = binding.editTextNomeProd.text.toString()
        val imagem = binding.imageInserir.toString()
        val valor = binding.editTextValorProd.text.toString()
        val descricao = binding.editTextDescricao.text.toString()
        val quantidade = binding.editTextQuantidade.text.toString()



        if (validarCampos(nome, imagem, valor, descricao, quantidade)) {
            Toast.makeText(context, "PRODUTO CADASTRADO", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_novoProduto_to_catalogo)

        } else {
            Toast.makeText(context, "Por favor, revise os campos indicados.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}