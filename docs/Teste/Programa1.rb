class Conta       # Define classe Conta
  def initialize(numero = 0, agencia = 0, saldo = 0)
    @numero = numero
    @agencia = agencia
    @saldo = saldo
  end
  def creditar(valor)
    @saldo = @saldo + valor
  end
  def debitar(valor)
    @saldo = @saldo - valor
  end
  def set(numero)
    @numero = numero
  end
  def getNumero
    @numero
  end
  def getAgencia
    @agencia
  end
  def getSaldo
    @saldo
  end
end

class Banco
  @@Contas = []
  @@quantcontas = 0
  def initialize(agencia)
    @@agencia = agencia
  end
  def criaConta()
    @@Contas[@@quantcontas] = Conta.new(@@quantcontas,@@agencia)
    @@quantcontas = @@quantcontas + 1
  end
  def getConta(numero)
    a = @@Contas[numero]
  end
end

puts 'Digite o numero da agencia do novo banco: '
agencia = gets

Itau = Banco.new(agencia)
Itau.criaConta()

puts 'O saldo da nova conta é'
puts Itau.getConta(0).getSaldo