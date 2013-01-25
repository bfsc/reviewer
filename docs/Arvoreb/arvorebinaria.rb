class Arvore
  def initialize(numero = nill)
    @numero = numero
    @filho_esq = nil
    @filho_dir = nil
  end
  def busca(numero) #busca um numero e vai imprimindo
    if @numero == numero
      puts @numero
      elsif numero < @numero && @filho_esq != nil
        puts @numero
        @filho_esq.busca(numero)
      elsif numero > @numero && @filho_dir != nil
        puts @numero
        @filho_dir.busca(numero)
      elsif numero < @numero && @filho_esq == nil
        puts 'numero inesistente'
      else numero > @numero && @filho_dir == nil
        puts 'numero inesistente'
    end
  end
  def insere(numero) #insere um numero na arvore
    if @numero == nil
      @numero = numero
     elsif numero < @numero && @filho_esq != nil
        @filho_esq.insere(numero)
     elsif numero > @numero && @filho_dir != nil
        @filho_dir.insere(numero)
     elsif numero < @numero && @filho_esq == nil
        @filho_esq = Arvore.new(numero)
     else numero > @numero && @filho_dir == nil
        @filho_dir = Arvore.new(numero)
    end
  end
  def get()
    @numero
  end
end

a = Arvore.new(8)
a.insere(10)
a.insere(12)
a.insere(6)
a.insere(2)
a.insere(4)

numero = gets.to_i

a.busca(numero)